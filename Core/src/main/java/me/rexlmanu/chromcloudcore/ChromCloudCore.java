package me.rexlmanu.chromcloudcore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.rexlmanu.chromcloudcore.utility.string.StringUtils;

import java.util.concurrent.ThreadLocalRandom;

public final class ChromCloudCore {

    public static final Gson GSON;
    public static final JsonParser PARSER;
    public static final boolean EPOLL;

    public static final ThreadLocalRandom THREAD_LOCAL_RANDOM;
    public static final char[] RANDOM;


    static {
        GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        PARSER = new JsonParser();
        EPOLL = Epoll.isAvailable();
        THREAD_LOCAL_RANDOM = ThreadLocalRandom.current();
        RANDOM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!".toCharArray();
    }

    public static void sendHeader() {
        System.out.println(" ");
        System.out.println("   ____   _                                   ____   _                       _ \n" +
                "  / ___| | |__    _ __    ___    _ __ ___    / ___| | |   ___    _   _    __| |\n" +
                " | |     | '_ \\  | '__|  / _ \\  | '_ ` _ \\  | |     | |  / _ \\  | | | |  / _` |\n" +
                " | |___  | | | | | |    | (_) | | | | | | | | |___  | | | (_) | | |_| | | (_| |\n" +
                "  \\____| |_| |_| |_|     \\___/  |_| |_| |_|  \\____| |_|  \\___/   \\__,_|  \\__,_|\n" +
                "                                                                               \n" +
                "A cloud management software for docker");
        System.out.println(" ");
        final Package chromCloudCorePackage = ChromCloudCore.class.getPackage();
        System.out.println(" ChromCloud #" + chromCloudCorePackage.getSpecificationVersion() + " v" + chromCloudCorePackage.getImplementationVersion());
        System.out.println(" Copyright © 2018 Emmanuel L. | All rights reserved");
        System.out.println(" ");
        System.out.println(String.format(" ChromCloud is running on %s (%s\\%s) with architecture %s", StringUtils.OS_NAME, StringUtils.USER_NAME, StringUtils.OS_VERSION, StringUtils.OS_ARCH));
        System.out.println(String.format(" Java: %s (Recommend: Java 1.8.0 or higher)", StringUtils.JAVA_VERSION));
        System.out.println(" ");
    }

    public static void sendFooter() {
        System.out.println(" ");
        System.out.println("   ____   _                                   ____   _                       _ \n" +
                "  / ___| | |__    _ __    ___    _ __ ___    / ___| | |   ___    _   _    __| |\n" +
                " | |     | '_ \\  | '__|  / _ \\  | '_ ` _ \\  | |     | |  / _ \\  | | | |  / _` |\n" +
                " | |___  | | | | | |    | (_) | | | | | | | | |___  | | | (_) | | |_| | | (_| |\n" +
                "  \\____| |_| |_| |_|     \\___/  |_| |_| |_|  \\____| |_|  \\___/   \\__,_|  \\__,_|\n" +
                "                                                                               \n" +
                "A cloud management software for docker");
        System.out.println(" ");
        System.out.println(" ChromCloud stopped successful");
        System.out.println(" ");
    }

    public static EventLoopGroup eventLoopGroup() {
        return EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    public static EventLoopGroup eventLoopGroup(int threads) {
        return EPOLL ? new EpollEventLoopGroup(threads) : new NioEventLoopGroup(threads);
    }

    public static Class<? extends ServerSocketChannel> chromCloudServerSocketChanel() {
        return EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    public static Class<? extends SocketChannel> chromCloudClientSocketChannel() {
        return EPOLL ? EpollSocketChannel.class : NioSocketChannel.class;
    }

}
