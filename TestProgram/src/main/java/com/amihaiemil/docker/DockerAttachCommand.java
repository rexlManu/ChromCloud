package com.amihaiemil.docker;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;

public final class DockerAttachCommand {

    /**
     * Apache HttpClient which sends the requests.
     */
    private final HttpClient client;

    /**
     * Base URI.
     */
    private final URI baseUri;

    /**
     * Docker API.
     */
    private final Docker docker;

    private String containerId;

    private boolean logs, followStream, timestamps, stdout, stderr;
    private InputStream stdin;

    /**
     * Ctor.
     *
     * @param client  Given HTTP Client.
     * @param baseUri Base URI, ending with /containers.
     * @param dkr     Docker where these Containers are from.
     */
    DockerAttachCommand(final HttpClient client, final URI baseUri, final Docker dkr) {
        this.client = client;
        this.baseUri = baseUri;
        this.docker = dkr;
    }
//
//    public void exec() {
//        final URI uri;
//            uri = new UncheckedUriBuilder(
//                    this.baseUri.toString().concat("/attach")
//            ).addParameter("name", name)
//                    .build();
//        final HttpPost post = new HttpPost(uri);
//        try {
//            post.setEntity(new StringEntity(container.toString()));
//            post.setHeader(new BasicHeader("Content-Type", "application/json"));
//            final JsonObject json = this.client.execute(
//                    post,
//                    new ReadJsonObject(
//                            new MatchStatus(post.getURI(), HttpStatus.SC_CREATED)
//                    )
//            );
//            return new RtContainer(new Merged(json, container),
//                    this.client,
//                    URI.create(
//                            this.baseUri.toString() + "/" + json.getString("Id")
//                    ),
//                    this.docker
//            );
//        } finally {
//            post.releaseConnection();
//        }
//    }

    public Container create(final String image) throws IOException {
        return this.create(
                "",
                Json.createObjectBuilder()
                        .add("Image", image)
                        .build()
        );
    }

    public Container create(
            final String name, final String image
    ) throws IOException {
        return this.create(
                name,
                Json.createObjectBuilder()
                        .add("Image", image)
                        .build()
        );
    }

    public Container create(final JsonObject container) throws IOException {
        return this.create("", container);
    }

    public Container create(
            final String name, final JsonObject container
    ) throws IOException {
        final URI uri;
        if (!name.isEmpty()) {
            uri = new UncheckedUriBuilder(
                    this.baseUri.toString().concat("/create")
            ).addParameter("name", name)
                    .build();
        } else {
            uri = new UncheckedUriBuilder(
                    this.baseUri.toString().concat("/create")
            ).build();
        }
        final HttpPost post = new HttpPost(uri);
        try {
            post.setEntity(new StringEntity(container.toString()));
            post.setHeader(new BasicHeader("Content-Type", "application/json"));
            final JsonObject json = this.client.execute(
                    post,
                    new ReadJsonObject(
                            new MatchStatus(post.getURI(), HttpStatus.SC_CREATED)
                    )
            );
            return new RtContainer(new Merged(json, container),
                    this.client,
                    URI.create(
                            this.baseUri.toString() + "/" + json.getString("Id")
                    ),
                    this.docker
            );
        } finally {
            post.releaseConnection();
        }
    }

    public Iterator<Container> all() {
        return new ResourcesIterator<>(
                this.client,
                new HttpGet(this.baseUri.toString().concat("/json?all=true")),
                json -> new RtContainer(
                        json,
                        this.client,
                        URI.create(
                                this.baseUri.toString() + "/" + json.getString("Id")
                        ),
                        this.docker
                )
        );
    }

    public Iterator<Container> iterator() {
        return new ResourcesIterator<>(
                this.client,
                new HttpGet(this.baseUri.toString().concat("/json")),
                json -> new RtContainer(
                        json,
                        this.client,
                        URI.create(
                                this.baseUri.toString() + "/" + json.getString("Id")
                        ),
                        this.docker
                )
        );
    }

    public Docker docker() {
        return this.docker;
    }
}
