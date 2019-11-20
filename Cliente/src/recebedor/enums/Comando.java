package recebedor.enums;

public enum Comando {
	EXIBIR_OPCOES("--opcoes")
	,ENVIAR_ARQUIVO("--enviar_arquivo"),
	DESCONECTAR("--desconectar");
	
	public String comando;
	
	private Comando(String comando){
		this.comando = comando;
	}
	
	public String getComando() {
		return this.comando;
	}
	
	public static Comando toEnum(String comando) {
		if(comando == null) {
			return null;
		}
		
		for(Comando c: Comando.values()) {
			if(comando.equals(c.getComando())) {
				return c;
			}
		}
		
		throw new IllegalArgumentException("Comando inválido: " + comando);
	}
}
