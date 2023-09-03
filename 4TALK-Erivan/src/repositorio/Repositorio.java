package repositorio;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import modelo.Grupo;
import modelo.Individual;
import modelo.Mensagem;
import modelo.Participante;

public class Repositorio {
	private TreeMap<String, Participante> participantes  = new TreeMap<>();
	private ArrayList<Mensagem> mensagens  = new ArrayList<>();
	
	public Repositorio() {carregarObjetos();}
	
	public void carregarObjetos() {
		try {
			//caso os arquivos nao existam, serao criados vazios
			File f1 = new File( new File("./backup/mensagens.csv").getCanonicalPath() ) ; 
			File f2 = new File( new File("./backup/individuos.csv").getCanonicalPath() ) ; 
			File f3 = new File( new File("./backup/grupos.csv").getCanonicalPath() ) ; 
			if (!f1.exists() || !f2.exists() || !f3.exists() ) {
				FileWriter arquivo1 = new FileWriter(f1); arquivo1.close();
				FileWriter arquivo2 = new FileWriter(f2); arquivo2.close();
				FileWriter arquivo3 = new FileWriter(f3); arquivo3.close();
				return;
			}
		}
		catch(Exception ex)		{
			throw new RuntimeException("criacao dos arquivos vazios:"+ex.getMessage());
		}
		
		String linha;	
		String[] partes;	
		try	{
			String nome,senha,administrador;
			File f = new File( new File("./backup/individuos.csv").getCanonicalPath())  ;
			Scanner arquivo1 = new Scanner(f);	 //  pasta do projeto
			while(arquivo1.hasNextLine()) 	{
				linha = arquivo1.nextLine().trim();	
				partes = linha.split(";");
				//System.out.println(Arrays.toString(partes));
				nome = partes[0];
				senha = partes[1];
				administrador = partes[2];
				Individual ind = new Individual(nome,senha,Boolean.parseBoolean(administrador));
				this.add(ind);
			}
			arquivo1.close();
		}
		catch(Exception ex)		{
			throw new RuntimeException("leitura arquivo de individuos:"+ex.getMessage());
		}
		
		try	{
			String nome;
			Grupo grupo;
			Individual individuo;
			File f = new File( new File("./backup/grupos.csv").getCanonicalPath())  ;
			Scanner arquivo2 = new Scanner(f);	 //  pasta do projeto
			while(arquivo2.hasNextLine()) 	{
				linha = arquivo2.nextLine().trim();	
				partes = linha.split(";");
				//System.out.println(Arrays.toString(partes));
				nome = partes[0];
				grupo = new Grupo(nome);
				if(partes.length>1)
					for(int i=1; i< partes.length; i++) {
						individuo = this.localizarIndividual(partes[i]);
						grupo.add(individuo);
						individuo.add(grupo);
					}
				this.add(grupo);
			}
			arquivo2.close();
		}
		catch(Exception ex)		{
			throw new RuntimeException("leitura arquivo de grupos:"+ex.getMessage());
		}
		
		try	{
			String nomeemitente, nomedestinatario,texto;
			int id;
			Mensagem m;
			Participante emitente,destinatario;
			File f = new File( new File("./backup/mensagens.csv").getCanonicalPath() )  ;
			Scanner arquivo3 = new Scanner(f);	 //  pasta do projeto
			while(arquivo3.hasNextLine()) 	{
				linha = arquivo3.nextLine().trim();		
				partes = linha.split(";");	
				//System.out.println(Arrays.toString(partes));
				id = Integer.parseInt(partes[0]);
				texto = partes[1];
				nomeemitente = partes[2];
				nomedestinatario = partes[3];
				emitente = this.localizarParticipante(nomeemitente);
				destinatario = this.localizarParticipante(nomedestinatario);
				m = new Mensagem(id,texto,emitente,destinatario);
				this.add(m);
				emitente.addEnviada(m);
				destinatario.addRecebida(m);
			} 
			arquivo3.close();
		}
		catch(Exception ex)		{
			throw new RuntimeException("leitura arquivo de mensagens:"+ex.getMessage());
		}
	}
	
	public void salvarObjetos() {
		//gravar nos arquivos csv os objetos que estão no repositório
		try	{
			File f = new File( new File("./backup/mensagens.csv").getCanonicalPath())  ;
			FileWriter arquivo1 = new FileWriter(f); 
			for(Mensagem m : mensagens) 	{
				arquivo1.write(	m.getId()+";"+
						m.getTexto()+";"+
						m.getEmitente().getNome()+";"+
						m.getDestinatario().getNome()+";"+
						m.getData()+"\n");	
			} 
			arquivo1.close();
		}
		catch(Exception e){
			throw new RuntimeException("problema na criação do arquivo  mensagens "+e.getMessage());
		}

		try	{
			File f = new File( new File("./backup/individuos.csv").getCanonicalPath())  ;
			FileWriter arquivo2 = new FileWriter(f) ; 
			for(Individual ind : this.getIndividuos().values()) {
				arquivo2.write(ind.getNome() +";"+ ind.getSenha() +";"+ ind.getAdministrador() +"\n");	
			} 
			arquivo2.close();
		}
		catch (Exception e) {
			throw new RuntimeException("problema na criação do arquivo  individuos "+e.getMessage());
		}

		try	{
			File f = new File( new File("./backup/grupos.csv").getCanonicalPath())  ;
			FileWriter arquivo3 = new FileWriter(f) ; 
			for(Grupo g : this.getGrupos().values()) {
				String texto="";
				for(Individual ind : g.getIndividuos())
					texto += ";" + ind.getNome();
				arquivo3.write(g.getNome() + texto + "\n");	
			} 
			arquivo3.close();
		}
		catch (Exception e) {
			throw new RuntimeException("problema na criação do arquivo  grupos "+e.getMessage());
		}
	}
	
	private Participante localizarParticipante(String nome) {
		return getParticipantes().get(nome);
	}

	private Individual localizarIndividual(String nome) {
		return getIndividuos().get(nome);
	}
	
	public void add(Participante p){
		participantes.put(p.getNome(), p);
	}
	
	public void add(Mensagem m) {
		mensagens.add(m);
	}
	
	public int next_mensagem_id() {
		if(mensagens.isEmpty())
			return 1;
		return mensagens.size()+1;
	}

	public TreeMap<String, Participante> getParticipantes() {
		return participantes;
	}
	
	public TreeMap<String, Grupo> getGrupos(){
		TreeMap<String, Grupo> grupos = new TreeMap<String, Grupo>();
		for(Map.Entry<String, Participante> i : getParticipantes().entrySet()) {
			if(i.getValue() instanceof Grupo)
				grupos.put(i.getKey(), (Grupo) i.getValue());
		}
		return grupos;
	}
	
	public TreeMap<String, Individual> getIndividuos(){
		TreeMap<String, Individual> individuos = new TreeMap<String, Individual>();
		for(Map.Entry<String, Participante> i : getParticipantes().entrySet()) {
			if(i.getValue() instanceof Individual)
				individuos.put(i.getKey(), (Individual) i.getValue());
		}
		return individuos;
	}

	public ArrayList<Mensagem> getMensagens() {
		return mensagens;
	}	
}
