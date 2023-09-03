package modelo;

import java.util.ArrayList;

public class Individual extends Participante {
	private String senha;
	private boolean administrador;
	private ArrayList<Grupo> grupos;
	
	public Individual(String n, String s, boolean adm) {
		super(n);
		senha = s;
		administrador = adm;
		grupos = new ArrayList<>();
	}
	
	public void add(Grupo g) {
		grupos.add(g);
	}

	@Override
	public String toString() {
		String admin;
		String g = "";
		if(administrador)
			admin = "sim";
		else
			admin = "nao";
		if(!grupos.isEmpty()) {
			for(Grupo i : grupos)
				g += i.getNome() + ", ";
			g = g.substring(0, g.length() - 2);
		}
		return nome+" = [Senha: " + senha + ", Administrador: " + admin + ", Inserido em ("+ g+")\n";
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean getAdministrador() {
		return administrador;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
	}

	public ArrayList<Mensagem> getEnviadas() {
		return enviadas;
	}

	public void setEnviadas(ArrayList<Mensagem> enviadas) {
		this.enviadas = enviadas;
	}

	public ArrayList<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(ArrayList<Grupo> grupos) {
		this.grupos = grupos;
	}

}
