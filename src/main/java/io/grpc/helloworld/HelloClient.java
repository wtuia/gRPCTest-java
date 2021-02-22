
package io.grpc.helloworld;


import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class HelloClient {
	
	private final GreeterGrpc.GreeterBlockingStub blockingStub;
	
	public HelloClient(Channel channel) {
		this.blockingStub = GreeterGrpc.newBlockingStub(channel);
	}
	
	public void greet(String name) {
		System.out.println("will try to greet :" + name);
		HelloRequest request = HelloRequest.newBuilder().setName(name).build();
		HelloReply response;
		try {
			response = blockingStub.sayHello(request);
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Greeting :" + response.getMessage());
		
		try {
			response = blockingStub.sayHelloAgin(request);
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Greeting :" + response.getMessage());
	}
	
	public static void main(String[] args) throws InterruptedException {
		String user = "world";
		String target = "127.0.0.1:50051";
		ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
				.usePlaintext()
				.build();
		try {
			HelloClient client = new HelloClient(channel);
			client.greet(user);
		}finally {
			channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
		}
		
		
	}
}
