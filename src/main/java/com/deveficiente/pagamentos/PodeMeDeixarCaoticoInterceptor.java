package com.deveficiente.pagamentos;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PodeMeDeixarCaoticoInterceptor extends HandlerInterceptorAdapter{
	
	private static final Logger log = LoggerFactory
			.getLogger(PodeMeDeixarCaoticoInterceptor.class);
	
	private Environment env;


	public PodeMeDeixarCaoticoInterceptor(Environment env) {
		this.env = env;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			Boolean caosLiberado = env.getProperty("enderecos-externos.caos-liberado",
					Boolean.class, false);
			log.info("Caos está liberado => {}",caosLiberado);
			
			if(caosLiberado && method.getBean() instanceof PodeMeDeixarCaotico) {
				talvezARequisicaoFiqueLenta();			
				talvezSolteException();			
			}			
		}
		
		return true;
	}
	
	private Random random = new Random();
	
	private void talvezSolteException() {
		int numero = random.nextInt(10);
		if(numero % 3 == 0) {
			throw new RuntimeException("O caos está rolando...");
		}
	}
	
	private void talvezARequisicaoFiqueLenta() {
		int numero = random.nextInt(10);
		if(numero % 3 == 0) {
			try {
				log.info("Atrasando a request em {} segundos",numero);
				Thread.sleep(numero * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
