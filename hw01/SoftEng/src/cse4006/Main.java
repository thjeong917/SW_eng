package cse4006;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FriendGraph graph = new FriendGraph();
		Person john = new Person("John");
		Person tom = new Person("Tom");
		Person jane = new Person("Jane");
		Person marry = new Person("Marry");
		graph.addPerson(john);
		graph.addPerson(tom);
		graph.addPerson(jane);
		graph.addPerson(marry);
		graph.addFriendship("John", "Tom");
		graph.addFriendship("Tom", "Jane");

//		System.out.println(john.name);
//		System.out.println(graph.plist[1].name.equals("Tom"));
		
//		for(int i=0;i<50;i++){
//			System.out.println(graph.plist[i].name);
//		}
		
//		int i=0, j=0;
//		for(i=0;i<50;i++){
//			for(j=0;j<50;j++){
//				System.out.print(graph.Friendship[i][j]+" ");
//			}
//			System.out.println();
//		}
		System.out.println(graph.getDistance("John", "Tom")); // 1
		System.out.println(graph.getDistance("John", "Jane")); // 2
		System.out.println(graph.getDistance("John", "John")); // -1
		System.out.println(graph.getDistance("John", "Marry")); // 0
	}
}
