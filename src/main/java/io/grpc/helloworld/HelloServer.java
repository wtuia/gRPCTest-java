package io.grpc.helloworld;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HelloServer {
	Server server;
	private void start() throws IOException {
		int port = 50051;
		server = ServerBuilder
				.forPort(port)
				.addService(new GreeterImpl())
				.build()
				.start();
		System.out.println("server listeneing on :" + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("shutdown gRPC service");
				try {
					HelloServer.this.shutdown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("shutdown");
			}
		});
	}
	
	
	private void shutdown() throws InterruptedException {
		if (server != null) {
			server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
		}
	}
	
	
	/**
	 * Await termination on the main thread since the grpc library uses daemon threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}
	
	static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
		@Override
		public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
			HelloReply reply = HelloReply.newBuilder().setMessage("[Java] Hello " + req.getName()).build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		}
		
		@Override
		public void sayHelloAgin(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
			HelloReply reply = HelloReply.newBuilder().setMessage("[Java] again Hello " + req.getName()).build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		}
	}
	
	public static void main(String[] args) throws Exception {
		final HelloServer server = new HelloServer();
		server.start();
		server.blockUntilShutdown();
	}
}

