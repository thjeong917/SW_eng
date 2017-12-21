package faceduck.ai;

import faceduck.actors.Grass;
import faceduck.actors.RabbitImpl;
import faceduck.commands.BreedCommand;
import faceduck.commands.EatCommand;
import faceduck.commands.MoveCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

public class RabbitAI extends AbstractAI implements AI {

	/**
	 * constructor for RabbitAI
	 */
	public RabbitAI() {
	}
	
	/**
	 * Rabbit has 3 command options as Animal, Breed, Eat, and Move.
	 * 
	 * At first, Rabbit will perform breed when there is vacant adjacent location.
	 * If breeding is unable, Rabbit will find adjacent grass and perform eat.
	 * If breeding in unable and eating is also unable, Rabbit will move to
	 * vacant direction.
	 * 
	 * If none of these commands are available, Rabbit's act will return null.
	 * 
	 */
	@Override
	public Command act(World world, Actor actor) {
		Location loc = world.getLocation(actor);

		int BREED_LIMIT = ((RabbitImpl) actor).getBreedLimit();
		int CURRENT_ENERGY = ((RabbitImpl) actor).getEnergy();

		// if dir == null, it means all directions are possessed by something
		Direction vacantdir = getVacantDir(world, loc);
		Direction grassdir = findPrey(world, loc, new Grass());

		// 1. breed
		if (CURRENT_ENERGY > BREED_LIMIT && vacantdir != null) {
//			System.out.println("Rabbit act 1");
			BreedCommand breed = new BreedCommand(vacantdir);
			return breed;
		}
		// 2. Eat
		else if (grassdir != null) {
//			System.out.println("Rabbit act 2");
			EatCommand eat = new EatCommand(grassdir);
			return eat;
		}
		// 3. move
		else if (vacantdir != null) {
//			System.out.println("Rabbit act 3");
			Direction dir = getPreyDir(world, actor, new Grass());
			Location dest = new Location(loc, dir);
			if (world.isValidLocation(dest)) {
				if (world.getThing(dest) != null) {
					dir = vacantdir;
				}
			}
			else if(!world.isValidLocation(dest))
				dir = vacantdir;
			
			MoveCommand move = new MoveCommand(dir);
			return move;
			// not done yet, still need improvement
		}
		// 4. none of above
		else {
//			System.out.println("Rabbit act none");
			return null;
		}
	}
}
