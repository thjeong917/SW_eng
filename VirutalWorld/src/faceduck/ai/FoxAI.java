package faceduck.ai;

import faceduck.actors.FoxImpl;
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

public class FoxAI extends AbstractAI implements AI {

	/**
	 * constructor for FoxAI
	 */
	public FoxAI() {
	}

	/**
	 * Fox has 3 command options as Animal, Breed, Eat, and Move.
	 * 
	 * At first, Fox will perform breed when there is vacant adjacent location.
	 * If breeding is unable, Fox will find adjacent Rabbit and perform eat.
	 * If breeding in unable and eating is also unable, Fox will move to
	 * vacant direction.
	 * 
	 * If none of these commands are available, Rabbit's act will return null.
	 * 
	 */
	@Override
	public Command act(World world, Actor actor) {
		Location loc = world.getLocation(actor);
		
		int BREED_LIMIT = ((FoxImpl)actor).getBreedLimit();
		int CURRENT_ENERGY = ((FoxImpl)actor).getEnergy();
		
		// if dir == null, it means all directions are possessed by something
		Direction vacantdir = getVacantDir(world, loc);
		Direction rabbitdir = findPrey(world, loc, new RabbitImpl());

		// 1. breed
		if (CURRENT_ENERGY > BREED_LIMIT && vacantdir != null) {
			BreedCommand breed = new BreedCommand(vacantdir);
			return breed;
		}
		// 2. Eat
		else if (rabbitdir != null) {
			EatCommand eat = new EatCommand(rabbitdir);
			return eat;
		}
		// 3. move
		else if (vacantdir != null) {
			Direction dir = getPreyDir(world, actor, new RabbitImpl());
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
			return null;
		}
	}

}
