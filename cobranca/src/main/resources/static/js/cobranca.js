$('#confirmacaoExclusaoModal').on('show.bs.modal', function(event){
	
	var button = $(event.relatedTarget);
	
	//data-codigo é recuperado como 'codigo'
	var codigoTitulo = button.data('codigo');
	var descricaoTitulo = button.data('descricao');
	
	var modal = $(this);
	var form = modal.find('form');
	var action = form.attr('action');
	if(!action.endsWith('/')){
		action += '/';
	}
	form.attr('action', action + codigoTitulo);
	
	modal.find('.modal-body span').html('Tem certeza que deseja excluir o titulo <b>' 
			+ descricaoTitulo + '</b>?');
	
});









function testarJavascriptAlert(){
	alert("Javascript carregado com sucesso!");
}

