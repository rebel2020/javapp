package com.thrillio.bgjobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.thrillio.dao.BookmarkDao;
import com.thrillio.entities.Weblink;
import com.thrillio.utils.HttpConnect;
import com.thrillio.utils.IOUtil;

public class DownloadWeblinks implements Runnable {
	private static long TIME_FRAME = 3000000000L;// 3 sec
	private boolean downloadAll = false;
	ExecutorService executer = Executors.newFixedThreadPool(5);

//	private List<Weblink> weblinks;
	public DownloadWeblinks(boolean downloadAll) {
		this.downloadAll = downloadAll;
	}

	public static class Downloader<T extends Weblink> implements Callable<T> {
		private T weblink;

		Downloader(T weblink) {
			this.weblink = weblink;
		}

		@Override
		public T call() throws Exception {
			weblink.setDownloadStatus(Weblink.DownloadStatus.FAILED);
			String htmlPage = HttpConnect.download(weblink.getUrl());
			if (htmlPage != null) {
//				System.out.println("Downloading page..." + weblink.getUrl());
				weblink.setHtmlPage(htmlPage);
				IOUtil.write(weblink.getHtmlPage(), weblink.getId());
			} else {
//				System.out.println("Error in downloading page.." + weblink.getUrl());
			}
			return weblink;
		}

	}

	public DownloadWeblinks() {
	}

	@Override
	public void run() {
		long endTime = System.nanoTime() + TIME_FRAME;
		while (!Thread.currentThread().isInterrupted()) {
			List<Weblink> weblinks = getWeblinks();
			if (weblinks.size() > 0) {
				download(weblinks);
			} else {
				System.out.println("No new weblink to download"+Thread.currentThread());
			}
			try {
				TimeUnit.SECONDS.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		executer.shutdown();
	}

	private void download(List<Weblink> weblinks) {
		List<Downloader<Weblink>> tasks = getTasks(weblinks);
		List<Future<Weblink>> futures = new ArrayList<>();
		try {
			futures = executer.invokeAll(tasks, TIME_FRAME, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Future<Weblink> future : futures) {
			try {
				if (!future.isCancelled()) {
					Weblink weblink = future.get();
					String htmlPage = weblink.getHtmlPage();
					if(htmlPage!=null) {
						IOUtil.write(htmlPage, weblink.getId());
						weblink.setDownloadStatus(Weblink.DownloadStatus.SUCCESS);
						System.out.println("Download successful..."+weblink.getUrl());
					}else {
						System.out.println("Webpage not downloaded..."+Thread.currentThread());
					}
				}else {
					System.out.println("Task is cancelled.."+Thread.currentThread());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private List<Downloader<Weblink>> getTasks(List<Weblink> weblinks) {
		List<Downloader<Weblink>> tasks = new ArrayList<>();
		for (Weblink weblink : weblinks) {
			tasks.add(new Downloader<Weblink>(weblink));
		}
		return tasks;
	}

	List<Weblink> getWeblinks() {
		List<Weblink> weblinks = new ArrayList<>();
		if (downloadAll == true) {
			weblinks = (new BookmarkDao()).getAllWeblinks();
			downloadAll = false;
		} else {
			weblinks = (new BookmarkDao()).getWeblniks(Weblink.DownloadStatus.NOT_ATTEMPTED);
		}
		return weblinks;
	}
}
