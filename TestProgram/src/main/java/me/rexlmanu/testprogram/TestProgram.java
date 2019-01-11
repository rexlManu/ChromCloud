package me.rexlmanu.testprogram;

import java.io.IOException;

public final class TestProgram {

    public static void main(String[] args) throws IOException {

        String rawCommandsList = "docker,run,-d,-it,-v,/home/Subnode/ChromCloud/servers/1/temp:/data,-it,-e,EULA=TRUE,-e,SPIGOT_DOWNLOAD_URL=https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar,-e,TYPE=spigot,-e,VERSION=1.8,-p,25565:25565,--name,mc-1,itzg/minecraft-server";
        System.out.println(rawCommandsList.replace(",", " "));
//        final String[] rawList = rawCommandsList.split(",");
//
//        System.out.println("-");
//        for (String s : rawList) {
//            System.out.println(s);
//        }
//        System.out.println("-");

        //            final Process process = new ProcessBuilder(rawList).directory(new File("/home/Subnode/ChromCloud/servers/1/temp/")).redirectErrorStream(true).start();
//            final Process process = new ProcessBuilder("docker", "attach", "mc-1").directory(new File("/home/Subnode/ChromCloud/servers/1/temp/")).redirectErrorStream(true).start();
//            final Process process = new ProcessBuilder("docker", "logs", "mc-1").directory(new File("/home/Subnode/ChromCloud/servers/1/temp/")).redirectErrorStream(true).start();

//        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://localhost:2375").build();
//        PipedOutputStream out = new PipedOutputStream();
//        PipedInputStream in = new PipedInputStream(out);
//
//        dockerClient.attachContainerCmd("mc-1")
//                .withStdErr(true)
//                .withStdOut(true)
//                .withFollowStream(true)
//                .withStdIn(in)
//                .exec(new Penis());
//
//        out.write(("op rexlManu" + "\n").getBytes());
//        out.flush();

//        final Docker docker = new LocalDocker(new File("/var/run/docker.sock"));
//        docker.containers().all().forEachRemaining(container -> {
//            System.out.println(container.containerId());
//            container.execCommand();
//        });

    }

//    public static class Penis extends AttachContainerResultCallback {
//        @Override
//        public void onNext(Frame item) {
//            System.out.println(new String(item.getPayload()));
//        }
//    }
}
