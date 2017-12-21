package faceduck.ai;

import faceduck.actors.Gnat;
import faceduck.actors.Grass;
import faceduck.commands.EatCommand;
import faceduck.commands.FoxtoGrassCommand;
import faceduck.commands.RabbittoGrassCommand;
import faceduck.commands.TeleportCommand;
import faceduck.commands.TooManyGrassCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.Fox;
import faceduck.skeleton.interfaces.Rabbit;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

/**
 * When energy is even, Almighty will check the balance of world and
 * do some godWork. If balance is okay, Almighty will perform teleport.
 * 
 * When energy is odd, Almighty will kill Gnat inside his viewRange. If there is
 * no Gnat nearby, Almighty will perform teleport.
 *
 */
public class AlmightyAI extends AbstractAI implements AI {
	private int countGnat = 0;
	
	/**
	 * Almighty can execute 5 Commands.
	 * 
	 * Fox_to_Grass() : When number of fox exceeds rabbit, transform foxes 
	 * into grass inside viewRange.
	 * 
	 * Rabbit_to_Grass() : When number of rabbit exceeds grass, transform rabbits 
	 * into grass inside viewRange.
	 * 
	 * Too_Many_Grass() : When number of grass exceeds 4 times of rabbit's number, 
	 * find a fox and transform grass into rabbits.
	 * If there is no fox in the world, make new fox.
	 * 
	 * eat() : kills Gnats at most 20.
	 * 
	 * teleport() : moves to random location.
	 * 
	 */
	@Override
	public Command act(World world, Actor actor) {
		// TODO Auto-generated method stub
		
		int cur_energy = ((Animal)actor).getEnergy();
		int view = actor.getViewRange();
		int rabbitNum = 0;
		int foxNum = 0;
		int grassNum = 0;
		Location curLoc = world.getLocation(actor);
		
		for(int x = 0; x <= world.getWidth() + view; x++){
			for (int y = 0; y <= world.getHeight() + view; y++){
				Location temp = new Location(x,y);
				if(world.isValidLocation(temp)){
					if(world.getThing(temp) instanceof Rabbit)
						rabbitNum++;
					else if(world.getThing(temp) instanceof Fox)
						foxNum++;
					else if(world.getThing(temp) instanceof Grass)
						grassNum++;
				}
			}
		}
		
		if ((cur_energy % 2) == 0) {
			if (foxNum >= rabbitNum) {
				FoxtoGrassCommand FtoG = new FoxtoGrassCommand(curLoc);
				return FtoG;
			}

			// 2. rabbitNum >= grassNum , need to transform rabbits into grass
			else if (rabbitNum >= grassNum) {
				RabbittoGrassCommand RtoG = new RabbittoGrassCommand(curLoc);
				return RtoG;
			}

			// 3. grassNum >= 4*rabbitNum , need to transform grass into rabbits
			// for foxes
			else if (grassNum >= 4 * rabbitNum) {
				TooManyGrassCommand TMG = new TooManyGrassCommand(curLoc);
				return TMG;
			} 
			else {
				TeleportCommand teleport = new TeleportCommand();
				return teleport;
			}

		} 
		else {
			if (findGnat(world, actor) && countGnat < 20) {
				System.out.println("Gnat killed!");
				EatCommand killGnat = new EatCommand(Direction.SOUTH);
				countGnat++;
				return killGnat;
			} else {

				TeleportCommand teleport = new TeleportCommand();
				return teleport;
			}
		}
	}
	
	/**
	 * finds Gnat inside actor's viewRange
	 * 
	 * @return 
	 * 		if there is Gnat inside viewRange, return true.
	 * 		else, return false.
	 */
	private boolean findGnat(World world, Actor actor) {
		Location curLoc = world.getLocation(actor);
		int view = actor.getViewRange();
		
		for(int x = curLoc.getX() - view; x <= curLoc.getX() + view; x++){
			for (int y = curLoc.getY() - view; y <= curLoc.getY() + view; y++){
				Location temp = new Location(x,y);
				if(world.isValidLocation(temp)){
					if(world.getThing(temp) instanceof Gnat){
						System.out.println("found Gnat");
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
