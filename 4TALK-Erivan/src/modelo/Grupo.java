package modelo;

import java.util.ArrayList;

public class Grupo extends Participante{
	private ArrayList<Individual> individuos;
	
	public Grupo(String n) {
		super(n);
		individuos = new ArrayList<>();
	}
	
	public void add(Individual i) {
		individuos.add(i);
	}

	@Override
	public String toString() {
		String indi = "";
		if(!individuos.isEmpty()) {
			for(Individual i : individuos)
				indi += i.getNome() + ", ";
			indi = indi.substring(0, indi.length() - 2);
		}
		return nome+" = individuos: (" + indi + ")";
	}

	public ArrayList<Individual> getIndividuos() {
		return individuos;
	}

	public void setIndividuos(ArrayList<Individual> individuos) {
		this.individuos = individuos;
	}
	
}
