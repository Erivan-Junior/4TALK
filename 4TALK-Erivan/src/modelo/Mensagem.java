package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensagem {
	private int id;
	private String texto;
	private Participante destinatario;
	private Participante emitente;
	private String data;
	
	public Mensagem(int id, String t, Participante e, Participante d) {
		this.id = id;
		texto = t;
		emitente = e;
		destinatario = d;
		data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
	}

	@Override
	public String toString() {
		return id+"(" + data+ ")[" + emitente.getNome() + "]: "+ texto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Participante getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Participante destinatario) {
		this.destinatario = destinatario;
	}

	public Participante getEmitente() {
		return emitente;
	}

	public void setEmitente(Participante emitente) {
		this.emitente = emitente;
	}
}
