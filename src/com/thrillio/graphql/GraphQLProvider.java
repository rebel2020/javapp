package com.thrillio.graphql;

import java.io.IOException;
import java.net.URL;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;

public class GraphQLProvider {

    private GraphQL graphQL;

    public GraphQL graphQL() { 
        return graphQL;
    }

    public void init() throws IOException {
/*        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();*/
    }

    private GraphQLSchema buildSchema(String sdl) {
		return null;
      // TODO: we will create the schema here later 
    }
}