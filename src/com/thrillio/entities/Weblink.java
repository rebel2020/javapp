package com.thrillio.entities;

import com.thrillio.partners.Sharable;

public class Weblink extends Bookmark implements Sharable {
	private String url;
	private String host;
	private String htmlPage;
	private DownloadStatus downloadStatus = DownloadStatus.NOT_ATTEMPTED;

	public enum DownloadStatus {
		NOT_ATTEMPTED, SUCCESS, FAILED, NOT_ELIGIBLE;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Weblink [url=" + url + ", host=" + host + "]";
	}

	public String getHost() {
		return host;
	}

	public String getHtmlPage() {
		return htmlPage;
	}

	public DownloadStatus getDownloadStatus() {
		return downloadStatus;
	}

	public void setDownloadStatus(DownloadStatus downloadStatus) {
		this.downloadStatus = downloadStatus;
	}

	public void setHtmlPage(String htmlPage) {
		this.htmlPage = htmlPage;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public boolean isKidFriendkyEligible() {
		if (url.contains("porn") || getTitle().contains("porn") || host.contains("adult"))
			return false;

		return true;
	}

	@Override
	public String getItemData() {
		StringBuilder builder = new StringBuilder();
		builder.append("<item>");
		builder.append("<type>weblink</type>");
		builder.append("<title>").append(getTitle()).append("</title>");
		builder.append("<url>").append(url).append("</url>");
		builder.append("<host>").append(host).append("</host>");
		builder.append("</item>");
		return builder.toString();
	}

}
