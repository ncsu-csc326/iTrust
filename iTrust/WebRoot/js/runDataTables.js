$(function(){
	// Only makes the element into a DataTable.js if its not already
	if(!$.fn.dataTable.isDataTable( '.dt-compat' ) ) {
		$('.dt-compat').DataTable( {
		    responsive: true,
		    "dom": 'irtp'
		} );
	}
	
	if(!$.fn.dataTable.isDataTable( '.dt-compat-date' ) ) {
		$(".dt-compat-date").DataTable({
			"order" : [ [ 2, "desc" ] ],
			responsive: true,
			"dom": 'irtp'
		});
	}
	
	if(!$.fn.dataTable.isDataTable( '.dt-compat-codes' ) ) {
		$(".dt-compat-codes").DataTable({
			responsive: true,
			"dom": 'frtip'
		});
	}
	
	
});