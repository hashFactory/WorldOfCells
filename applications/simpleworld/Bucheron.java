
package applications.simpleworld;

import util.Case;

public class Bucheron extends Agent{
	
	int numero;

	int vie;

	int cptVie;

	Ville ville;

	Case tree;
	public Bucheron(int num, int _x, int _y, WorldOfTrees _world, Ville _ville){

		super(_x,_y,_world);
		this.numero = num;
		
		this.vie = 100;

		this.ville = _ville;

		cptVie = 0;
	}

	public void step(){
		if(cptVie == 100){
			this.vie--;			
			cptVie = 0;
		}
		if(tree == null){
			tree = this.ville.rechercheRandomCaseParNumero(1);
		}
		else{
			this.goTo(tree.x,tree.y,numero);
		}
		
	}
}
