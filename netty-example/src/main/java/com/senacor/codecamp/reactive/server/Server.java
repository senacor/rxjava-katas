package com.senacor.codecamp.reactive.server;

import com.senacor.codecamp.reactive.service.ArticleBeingReadService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.internal.SystemPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.io.File.separator;

/**
 * Setup Netty as a simple HTTP server.
 *
 * Serves a Frontend at:
 * http://localhost:8080/index.html
 *
 * Serves Web Socket requests at:
 * ws://localhost:8080/ws
 *
 * Default port is 8080. Can be changed via system property "port"
 *
 * @author Andri Bremm
 */
public final class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
    private static final String WEBSOCKET_PATH = "/ws";

    private static String STATIC_FILE_ROOT_DIR = SystemPropertyUtil.get("user.dir") + separator + "netty-example" + separator + "static";

    public static void main(String[] args) throws Exception {
        // TODO: replace quick fix
        STATIC_FILE_ROOT_DIR = STATIC_FILE_ROOT_DIR.replace("netty-example" + separator+ "netty-example", "netty-example");
        System.out.println("STATIC_FILE_ROOT_DIR=" + STATIC_FILE_ROOT_DIR);

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // Starting Netty
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline pipeline = ch.pipeline();
                     pipeline.addLast(new HttpServerCodec());
                     pipeline.addLast(new HttpObjectAggregator(65536));

                     // web sockets
                     pipeline.addLast(new WebSocketServerCompressionHandler());
                     pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
                     pipeline.addLast(new WebSocketFrameHandler()
                             .bind("articleBeingRead", () -> new ArticleBeingReadService().articleBeingReadObservable()));

                     // http for static files
                     pipeline.addLast(new ChunkedWriteHandler());
                     pipeline.addLast(new StaticFileServerHandler(STATIC_FILE_ROOT_DIR));
                 }
             });

            Channel ch = b.bind(PORT).sync().channel();

            logger.info("Find frontend at http://localhost:{}/index.html", PORT);
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
