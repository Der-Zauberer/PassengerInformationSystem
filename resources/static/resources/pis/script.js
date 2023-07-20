class PIS {
	
	search() {
		const input = document.getElementById('search');
		console.log(input);
		console.log(input.value);
		input.value ? swd.setParameter('query', input.value) : swd.removeParameter('query');
	}
	
}

const pis = new PIS();