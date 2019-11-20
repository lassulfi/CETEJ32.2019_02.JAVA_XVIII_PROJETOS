package br.com.caleum.model;

public class Conta {
	
	private int tipo;
	private int numero;
	private int agencia;
	private String titular;
	private double saldo;
	
	public Conta() {
		super();
	}
	
	public boolean sacar(double valor) {
		if(this.saldo > valor) {
			this.saldo -= valor;
			return true;
		} else {
			return false;
		}
	}
	
	public void depositar(double valor) {
		this.saldo += valor;
	}
	
	public boolean transferir(Conta destino, double valor) {
		boolean saque = this.sacar(valor);
		if(saque) {
			destino.depositar(valor);
			return true;
		} else {
			return false;
		}
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getAgencia() {
		return agencia;
	}

	public void setAgencia(int agencia) {
		this.agencia = agencia;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
}
