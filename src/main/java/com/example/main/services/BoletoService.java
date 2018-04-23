package com.example.main.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.main.domain.PagamentoBoleto;

@Service
public class BoletoService {

	public void calcularDataVencimento(PagamentoBoleto pagamentoBoleto, Date dataPedido) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(dataPedido);
		instance.add(Calendar.DAY_OF_MONTH, 7);
		pagamentoBoleto.setDataVencimento(instance.getTime());
	}

}