package modelo;

import java.util.ArrayList;

public class Participante {
	protected String nome;
	protected ArrayList<Mensagem> recebidas;
	protected ArrayList<Mensagem> enviadas;
	
	public Participante(String n) {
		nome = n;
		recebidas = new ArrayList<>();
		enviadas = new ArrayList<>();
	}
	
	public void addEnviada(Mensagem m) {
		enviadas.add(m);
	}
	
	public void addRecebida(Mensagem m) {
		recebidas.add(m);
	}

	public ArrayList<Mensagem> getRecebidas() {
		return recebidas;
	}

	public void setRecebidas(ArrayList<Mensagem> recebidas) {
		this.recebidas = recebidas;
	}

	public ArrayList<Mensagem> getEnviadas() {
		return enviadas;
	}

	public void setEnviadas(ArrayList<Mensagem> enviadas) {
		this.enviadas = enviadas;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
