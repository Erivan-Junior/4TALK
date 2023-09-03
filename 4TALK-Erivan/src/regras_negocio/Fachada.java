package regras_negocio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

import modelo.*;
import repositorio.Repositorio;

public class Fachada {
	private static Repositorio repositorio = new Repositorio();
	public Fachada() {}
	
	public static void carregar() {
		repositorio.carregarObjetos();
	}
	public static void salvar() {
		repositorio.salvarObjetos();
	}
	
	public static void criarIndividuo(String nome, String senha) throws Exception {
		try {
			validarIndividuo(nome, senha);
			throw new Exception("Nome do Individuo ja existe");
		} catch (NullPointerException e) {
			throw new Exception(e.getMessage());
		}
		catch (SecurityException e) {
			repositorio.add(new Individual(nome, senha, false));
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public static Individual validarIndividuo(String nome, String senha) throws Exception{
		try {
			if(nome.isEmpty() && senha.isEmpty()) 
				throw new NullPointerException("s Nome e senha");
			if(nome.isEmpty()) 
				throw new NullPointerException(" Nome");
			if(senha.isEmpty()) 
				throw new NullPointerException(" Senha");
			
			Participante ind = repositorio.getParticipantes().get(nome);
			if(ind != null) {
				if(ind instanceof Grupo)
					throw new Exception("Nome do Individuo pertence a um grupo");
				else
					if(((Individual) ind).getSenha().equals(senha))
						return (Individual) ind;
					else
						throw new Exception("Senha Incorreta");
			}
			else
				throw new SecurityException("Nenhum registro encontrado");
		}catch (NullPointerException e) {
			throw new NullPointerException("Campo"+e.getMessage()+" do individuo vazio"+e.getMessage().substring(0,1));
		}
		catch (SecurityException e) {
			throw new SecurityException(e.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public static void criarAdministrador(String nome, String senha) throws Exception{
		try {
			validarIndividuo(nome, senha);
			throw new Exception("Nome do Individuo ja existe");
		} catch (NullPointerException e) {
			throw new Exception(e.getMessage());
		}
		catch (SecurityException e) {
			repositorio.add(new Individual(nome, senha, true));
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public static void criarGrupo(String nome) throws Exception{
		if(repositorio.getParticipantes().containsKey(nome))
			throw new Exception("Nome ja em uso");
		repositorio.add(new Grupo(nome));
	}
	
	public static void inserirGrupo(String nomeIndividual, String nomeGrupo) throws Exception{
		try {
			if(nomeGrupo.isEmpty() && nomeIndividual.isEmpty())
				throw new Exception("s Nome do grupo e Nome do individuo");
			if(nomeGrupo.isEmpty())
				throw new Exception(" Nome do grupo");
			if(nomeIndividual.isEmpty())
				throw new Exception(" Nome do individuo");
			
			TreeMap<String, Participante> participantes = repositorio.getParticipantes();
			if(!participantes.containsKey(nomeGrupo) && !participantes.containsKey(nomeIndividual))
				throw new Exception("-Grupo e Individuo");
			if(!participantes.containsKey(nomeGrupo))
				throw new Exception("-Grupo");
			if(!participantes.containsKey(nomeIndividual))
				throw new Exception("-Individuo");
			
			Grupo grupo = (Grupo) participantes.get(nomeGrupo);
			Individual individuo = (Individual) participantes.get(nomeIndividual);
			
			if(!grupo.getIndividuos().contains(individuo)) {
				grupo.getIndividuos().add(individuo);
				individuo.getGrupos().add(grupo);
			}
			else
				throw new Exception("!Individuo");
		} catch (Exception e) {
			if(e.getMessage().substring(0, 1).equals("!"))
				throw new Exception(e.getMessage().substring(1)+ " já existe no grupo");
			else if(e.getMessage().substring(0, 1).equals("-"))
				throw new Exception(e.getMessage().substring(1)+ " inexistente");
			else
				throw new Exception("Campo"+e.getMessage()+" vazio"+e.getMessage().substring(0,1));
		}
	}
	
	public static void removerGrupo(String nomeIndividual, String nomeGrupo) throws Exception{
		try {
			if(nomeGrupo.isEmpty() && nomeIndividual.isEmpty())
				throw new Exception("s Nome do grupo e Nome do individuo");
			if(nomeGrupo.isEmpty())
				throw new Exception(" Nome do grupo");
			if(nomeIndividual.isEmpty())
				throw new Exception(" Nome do individuo");

			TreeMap<String, Participante> participantes = repositorio.getParticipantes();
			if(!participantes.containsKey(nomeGrupo) && !participantes.containsKey(nomeIndividual))
				throw new Exception("-Grupo e Individuo");
			if(!participantes.containsKey(nomeGrupo))
				throw new Exception("-Grupo");
			if(!participantes.containsKey(nomeIndividual))
				throw new Exception("-Individuo");
			
			Grupo grupo = (Grupo) participantes.get(nomeGrupo);
			Individual individuo = (Individual) participantes.get(nomeIndividual);
			
			if(grupo.getIndividuos().contains(individuo)) {
				grupo.getIndividuos().remove(individuo);
				individuo.getGrupos().remove(grupo);
			}
			else
				throw new Exception("!Individuo");
		} catch (Exception e) {
			if(e.getMessage().substring(0, 1).equals("!"))
				throw new Exception(e.getMessage().substring(1)+ " não existe no grupo");
			else if(e.getMessage().substring(0, 1).equals("-"))
				throw new Exception(e.getMessage().substring(1)+ " inexistente");
			else
				throw new Exception("Campo"+e.getMessage()+" vazio"+e.getMessage().substring(0,1));
		}
	}
	
	public static void criarMensagem(String nomeEmitente, String nomeDestinatario, String texto) throws Exception{
		try {
			if(nomeEmitente.isEmpty() && nomeEmitente.isEmpty())
				throw new Exception("s Nome Emitende e Nome Destinatario");
			if(nomeEmitente.isEmpty())
				throw new Exception(" Nome Emitende");
			if(nomeEmitente.isEmpty())
				throw new Exception(" Nome Destinatario");
			if(texto.isEmpty())
				throw new Exception(" Texto");
			

			TreeMap<String, Participante> participantes = repositorio.getParticipantes();
			if(!participantes.containsKey(nomeEmitente) && !participantes.containsKey(nomeDestinatario))
				throw new Exception("-emitente e destinatario");
			if(!participantes.containsKey(nomeEmitente))
				throw new Exception("-emitente");
			if(!participantes.containsKey(nomeDestinatario))
				throw new Exception("-destinatario");
			
			Mensagem msgOriginal;
			Mensagem msgCopia;
			Individual emitente = (Individual) participantes.get(nomeEmitente);
			Participante destinatario = participantes.get(nomeDestinatario);
			
			if(destinatario instanceof Grupo) 
				if(!((Grupo) destinatario).getIndividuos().contains(emitente))
					throw new Exception("!");	
			
			msgOriginal = new Mensagem(repositorio.next_mensagem_id(), texto, emitente, destinatario);
			emitente.addEnviada(msgOriginal);
			destinatario.addRecebida(msgOriginal);
			repositorio.add(msgOriginal);
			
			if(destinatario instanceof Grupo) 
				for(Individual i : ((Grupo) destinatario).getIndividuos())
					if(!i.equals(emitente)) {
						msgCopia = new Mensagem(msgOriginal.getId(), texto+"\t["+destinatario.getNome()+"]", destinatario, i);
						i.addRecebida(msgCopia);
						destinatario.addEnviada(msgCopia);
						repositorio.add(msgCopia);
					}
		} catch (Exception e) {
			if(e.getMessage().substring(0, 1).equals("-"))
				throw new Exception("Participante " + e.getMessage().substring(1)+ " inexistente");
			else if(e.getMessage().substring(0, 1).equals("!"))
				throw new Exception("Individuo [" + nomeEmitente + "] não está inserido no grupo: " + nomeDestinatario);
			else
				throw new Exception("Campo"+e.getMessage()+" vazio"+e.getMessage().substring(0,1));
		}
	}
	
	public static ArrayList<Mensagem> obterConversa(String nomeEmitente, String nomeDestinatario) throws Exception {
		try {
			if(nomeEmitente.isEmpty() && nomeEmitente.isEmpty())
				throw new Exception("s Nome Emitende e Nome Destinatario");
			if(nomeEmitente.isEmpty())
				throw new Exception(" Nome Emitende");
			if(nomeEmitente.isEmpty())
				throw new Exception(" Nome Destinatario");

			TreeMap<String, Participante> participantes = repositorio.getParticipantes();
			if(!participantes.containsKey(nomeEmitente) && !participantes.containsKey(nomeDestinatario))
				throw new Exception("-emitente e destinatario");
			if(!participantes.containsKey(nomeEmitente))
				throw new Exception("-emitente");
			if(!participantes.containsKey(nomeDestinatario))
				throw new Exception("-destinatario");
				
			Individual emitente = (Individual) participantes.get(nomeEmitente);
			Participante destinatario = participantes.get(nomeDestinatario);
			ArrayList<Mensagem> mensagens = new ArrayList<>();
			
			if(destinatario instanceof Individual) {
				for(Mensagem m : emitente.getEnviadas())
					if(m.getDestinatario().equals(destinatario))
						mensagens.add(m);
				
				for(Mensagem m : destinatario.getEnviadas())
					if(m.getDestinatario().equals(emitente))
						mensagens.add(m);
			}
			else
				for(Mensagem m : destinatario.getRecebidas()) 
						mensagens.add(m);
			if(mensagens.isEmpty())
				throw new Exception("!");
			mensagens.sort(new Comparator<Mensagem>() {
				@Override
				public int compare(Mensagem o1, Mensagem o2) {
					 if (o1.getId() < o2.getId())
				            return -1;
				     else if (o1.getId() > o2.getId())
				            return 1;
				     else
				            return 0;
				}}
);
			return mensagens;
		} catch (Exception e) {
			if(e.getMessage().substring(0, 1).equals("-"))
				throw new Exception("Participante " + e.getMessage().substring(1)+ " inexistente");
			else if(e.getMessage().substring(0, 1).equals("!"))
				throw new Exception("Não há registros de conversa");
			else
				throw new Exception("Campo"+e.getMessage()+" vazio"+e.getMessage().substring(0,1));
		}
	}
	
	public static void apagarMensagem(String nomeIndividuo, Integer id) throws Exception {
		// o id é do tipo Integer para poder comparar com null, visto que pode acontecer do programa enviar um id null.
		try {
			if(nomeIndividuo.isEmpty() && id == null)
				throw new Exception("s Nome do individuo e ID");
			if(nomeIndividuo.isEmpty())
				throw new Exception(" Nome do individuo");
			if(id == null)
				throw new Exception(" ID");
			
			TreeMap<String, Participante> participante = repositorio.getParticipantes();
			ArrayList<Mensagem> mensagens = repositorio.getMensagens();
			int existe = 0;
			if(!participante.containsKey(nomeIndividuo))
				throw new Exception("-Individuo");
			for(Mensagem m : mensagens)
				if(m.getId() == id) {existe++;break;}
			if(existe == 0)
				throw new Exception("-ID");
			
			Individual emitente = (Individual) participante.get(nomeIndividuo);
			Mensagem mensagem = null;
			Participante destinatario;
						
			for(Mensagem m : emitente.getEnviadas())
				if(m.getId() == id)
					mensagem = m;
			if(mensagem == null)
				throw new Exception("!");
			
			for(Mensagem m: mensagens)
				if(m.getId() == id)
					mensagens.remove(m);
			destinatario = mensagem.getDestinatario();	
			if(destinatario instanceof Grupo)
				for(Mensagem m : destinatario.getEnviadas())
					if(m.getId() == id) {
						destinatario.getEnviadas().remove(m);
						m.getDestinatario().getRecebidas().remove(m);
					}
			emitente.getEnviadas().remove(mensagem);
			destinatario.getRecebidas().remove(mensagem);			
		} catch (Exception e) {
			if(e.getMessage() == null) {}
			else if(e.getMessage().equals("!"))
				throw new Exception("Individuo não possui mesagem enviada com o ID informado");
			else if(e.getMessage().substring(0, 1).equals("-"))
				throw new Exception(e.getMessage().substring(1)+ " inexistente");
			else
				throw new Exception("Campo"+e.getMessage()+" vazio"+e.getMessage().substring(0,1));
		}
	}
	
	public static ArrayList<Mensagem> listarMensagens() {
		return repositorio.getMensagens();
	}
	
	public static ArrayList<Mensagem> listarMensagensEnviadas(String nome) throws Exception{
		try {
			if(nome.isEmpty())
				throw new Exception(" Nome");
			
			TreeMap<String, Individual> individuos = repositorio.getIndividuos();
			if(!individuos.containsKey(nome))
				throw new Exception("!Individuo");
			
			return new ArrayList<>(repositorio.getIndividuos().get(nome).getEnviadas());
		} catch (Exception e) {
			if(e.getMessage().substring(0, 1).equals("!"))
				throw new Exception(e.getMessage().substring(1)+ " inexistente");
			else
				throw new Exception("Campo"+e.getMessage()+" vazio"+e.getMessage().substring(0,1));
		}
	}
	
	public static ArrayList<Mensagem> listarMensagensRecebidas(String nome) throws Exception {
		try {
			if(nome.isEmpty())
				throw new Exception(" Nome");
			
			TreeMap<String, Individual> individuos = repositorio.getIndividuos();
			if(!individuos.containsKey(nome))
				throw new Exception("!Individuo");
			
			return new ArrayList<>(repositorio.getIndividuos().get(nome).getRecebidas());
		} catch (Exception e) {
			if(e.getMessage().substring(0, 1).equals("!"))
				throw new Exception(e.getMessage().substring(1)+ " inexistente");
			else
				throw new Exception("Campo"+e.getMessage()+" vazio"+e.getMessage().substring(0,1));
		}
	}
	
	public static ArrayList<Individual> listarIndividuos() {
		return new ArrayList<Individual>(repositorio.getIndividuos().values());
	}
	
	public static ArrayList<Grupo> listarGrupos() {
		return new ArrayList<>(repositorio.getGrupos().values());
	}
	
	public static ArrayList<Mensagem> espionarMensagens(String nome, String termo) throws Exception {
		try {
			if(nome.isEmpty())
				throw new Exception("Campo nome está Vazio");
			
			Individual admin = validarIndividuo(nome, nome);
			ArrayList<Mensagem>msgEspiadas =  new ArrayList<Mensagem>();
			if(!admin.getAdministrador())
				throw new Exception("Permissão Negada");
			
			if(termo == null)
				return repositorio.getMensagens();
			for(Mensagem m : repositorio.getMensagens())
				if(m.getTexto().contains(termo))
					msgEspiadas.add(m);
			return msgEspiadas;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public static ArrayList<String> ausentes(String admin) throws Exception {
		try {
			if(validarIndividuo(admin, "admin") != null) {
				ArrayList<String> ausentes = new ArrayList<String>();
				for(Participante p : repositorio.getParticipantes().values())
					if(p.getEnviadas().isEmpty())
						ausentes.add(p.getNome());
				return ausentes;
			}
			else
				throw new Exception();
		} catch (Exception e) {
			throw new Exception("Permissão negada");
		}
	}
}
