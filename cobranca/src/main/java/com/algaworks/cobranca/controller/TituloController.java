package com.algaworks.cobranca.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.cobranca.model.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.Titulos;

@Controller
@RequestMapping("/titulos")
public class TituloController {
	
	// IOC/DI
	// padrao repository = org.springframework.data.jpa.repository.JpaRepository
	@Autowired
	private Titulos titulos; 
	
	private static final String CADASTRO_VIEW = "CadastroTitulo";
		

	@RequestMapping("/novo")
	public ModelAndView novo(){		
		ModelAndView modelAndView = new ModelAndView(CADASTRO_VIEW);		
		modelAndView.addObject(new Titulo());		
		return modelAndView;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes){	
				
		if(errors.hasErrors()){			
			exibirLogErros(errors);					
			return CADASTRO_VIEW;
		}
				
		Long id = titulos.save(titulo).getCodigo();				
		logTitulo(titulos.getOne(id));				
		
		//disponibiliza uma mensagemm para a nova requisicao
		attributes.addFlashAttribute("mensagemTH", "Título salvo com sucesso");		
		
		//nova requisicao para o formulario, assim limpa os campos para nova inclusao - status 300
		return "redirect:/titulos/novo";
	}
		
	//@RequestMapping
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView pesquisar(){		
		List<Titulo>todosTitulos = titulos.findAll();
		ModelAndView modelAndView = new ModelAndView("PesquisaTitulos");
		modelAndView.addObject("titulos", todosTitulos);
		
		return modelAndView;
	}
	
	/*
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable Long codigo){
		Titulo titulo = titulos.findOne(codigo);
		
		ModelAndView mav = new ModelAndView(CADASTRO_VIEW);
		mav.addObject(titulo);
		return mav;
	}
	*/
	
	//edicao refatorado
	//@PathVariable("codigo") faz um findOne(...)
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Titulo titulo){		
		ModelAndView mav = new ModelAndView(CADASTRO_VIEW);
		mav.addObject(titulo);
		return mav;
	}
	
	/*
	* Resolved exception caused by handler execution: 
	* org.springframework.web.HttpRequestMethodNotSupportedException: 
	* Request method 'DELETE' not supported
	*/
	//TODO request mapping para exclusao de titulo	
	@RequestMapping(value="{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo,  RedirectAttributes attributes){
		titulos.delete(codigo);
		
		//disponibiliza uma mensagemm para a nova requisicao
		attributes.addFlashAttribute("mensagemTH", "Título exclído com sucesso");	
		
		return "redirect:/titulos";
	}
	
	
	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo(){
		return Arrays.asList(StatusTitulo.values());
	}
		
	//	# log
	private void logTitulo(Titulo titulo){		
		System.out.println(" »»» ");
		System.out.println("getDescricao »»» " + titulo.getDescricao());
		System.out.println("getDataVencimento »»» " + titulo.getDataVencimento());
		System.out.println("getValor »»» " + titulo.getValor());
		System.out.println("getStatus »»» " + titulo.getStatus());
		System.out.println("getCodigo »»» " + titulo.getCodigo());				
	}
		
	//	# log
	private void exibirLogErros(Errors errors ){
		List<ObjectError> errs = errors.getAllErrors();			
		for(ObjectError e : errs){
			System.out.println("spring | errors.hasErrors() amigavel » " + e.getDefaultMessage());
			System.out.println("spring | errors.hasErrors() desenvolvedor » " + e.toString());
			
		}
	}
}
