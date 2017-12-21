package cse4006;
public class Queue {
	int[] queue;
	private int size = 0;
	private int front = -1;
	private int rear = -1;
	private int MAX = 0;
	
	Queue(int size) {
		this.size = size;
		this.queue = new int[size];
	}
	
	public boolean Full(){
		if (rear == size-1)
			return true;
		else
			return false;
	}
	public boolean Empty(){
		if (front == rear)
			return true;
		else
			return false;
	}
	
	public void enqueue(int a){
		if(Full()) {
			System.out.println("FULL!!");
			System.exit(0);
		}
		queue[++rear] = a;
	}
	public int dequeue(){
		if(Empty()){
			System.out.println("EMPTY!!");
			System.exit(0);
		}
		++front;
		int temp = queue[front];
		queue[front] = 0;
		
		if(Empty()){
			rear = -1;
			front = -1;
		}
		
		return temp;
	}
	
	public void flush(){
		this.queue = new int[size];
		rear = -1;
		front = -1;
	}
}
