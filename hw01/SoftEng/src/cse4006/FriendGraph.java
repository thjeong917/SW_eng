package cse4006;
public class FriendGraph {
	private int index = 0;
	private int MAXSIZE = 50;
	Person[] plist = new Person[MAXSIZE];
	private int[][] Friendship = new int[MAXSIZE][MAXSIZE];
	private int[] visited = new int[MAXSIZE];
	private int vertex = 0;
	Queue queue = new Queue(MAXSIZE);
	private int result = 0;

	public void addPerson(Person a) {
		plist[index] = a;
		index++;
	}

	public void addFriendship(String a, String b) {
		int num1 = -1, num2 = -1;
		for (int i = 0; i < index; i++) {
			if (plist[i].name.equals(a))
				num1 = i;
			if (plist[i].name.equals(b))
				num2 = i;
		}

		if (num1 == -1 || num2 == -1) {
			System.out.println("Name is not on the list");
			return;
		}

		this.Friendship[num1][num2] = 1;
		this.Friendship[num2][num1] = 1;
	}

	public int getDistance(String a, String b) {
		for (int i = 0; i < MAXSIZE; i++) {
			if (plist[i].name.equals(a)){
				vertex = i;
				plist[i].s_dist = 0;
//				System.out.println(i);
//				System.out.println(plist[i].name + "에서 시작함. 숫자는 " + vertex);
				break;
			}
		}
		visited[vertex] = 1;
		queue.enqueue(vertex); //rear가 ++되어 queue[0]에 vertex 저장
		int dist = 0;
		while(!queue.Empty()){
			vertex = queue.dequeue();
			for(int i = 0; i < MAXSIZE; i++){
				if(this.Friendship[vertex][i] == 1 && visited[i] == 0){
					visited[i] = 1;
//					System.out.println(vertex + "에서 " + i + "연결됨");
					queue.enqueue(i);
					dist++;
					plist[i].s_dist = dist;
				}
//				else
//					System.out.println("인접이 없음! 패쓰");
			}
		}
		
//		System.out.println("While Ended");
		
		for (int i=0; i<MAXSIZE; i++){
			if(plist[i].name.equals(b)){
//				System.out.println("Distance is " + plist[i].s_dist);
				result = plist[i].s_dist;
				break;
			}
		}
		queue.flush();
		for (int i=0; i<MAXSIZE; i++){
			visited[i] = 0;
		}
		
		return result;
	}
}
