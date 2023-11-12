class PIS {
	
	/*******************/
	/* Page            */
	/*******************/
	
	page(page) {
		const currentUrl = new URL(window.location.href);
		currentUrl.searchParams.set('page', page);
		window.location.href = currentUrl.toString();
	}
	
	/*******************/
	/* Search          */
	/*******************/
	
	search() {
		const input = document.getElementById('search');
		const currentUrl = new URL(window.location.href);
		currentUrl.searchParams.delete('page');
		currentUrl.searchParams.delete('pageSize');
		input.value ? currentUrl.searchParams.set('search', input.value) : currentUrl.searchParams.delete('search');
		window.location.href = currentUrl.toString();
	}
	
	resetSearch() {
		const currentUrl = new URL(window.location.href);
		currentUrl.searchParams.delete('search');
		currentUrl.searchParams.delete('page');
		currentUrl.searchParams.delete('pageSize');
		window.location.href = currentUrl.toString();
	}
	
	/*******************/
	/* Forms           */
	/*******************/
	
	initializeLeaveChecker() {
		window.onbeforeunload = (event) => {
			if (event.explicitOriginalTarget.type !== 'submit') {
				return this.#checkForDirtyForm();
			}
		};
		
	}
	
	#checkForDirtyForm() {
		let dirty = false;
		for (const input of document.getElementsByTagName('input')) {
			if (input.hasAttribute('dirty')) {
				dirty = true;
			}
		}
		return dirty ? "" : null;
	}
	
	/*******************/
	/* Import / Export */
	/*******************/
	
	#importExportBlocker = false;
	
	import(url) {
		if (this.#importExportBlocker) return;
		this.#importExportBlocker = true;
		const input = document.createElement('input');
		input.type = 'file';
		input.accept = "application/json"
		input.onchange = event => {
			if(!window.FileReader) return;
			const fileReader = new FileReader();
			fileReader.onload = function(event) {
				document.getElementById('import-export-notification-text').innerText = 'Importing ...';
				swd.toggleNotification('import-export-notification');
        		fetch(url, {method: 'POST', body: event.target.result, signal: AbortSignal.timeout(120000) }).then(event => {
					location.reload();
				});
    		};
    		fileReader.readAsText(event.target.files[0]);
		};
		input.click();
	}
	
	export(url) {
		if (this.#importExportBlocker) return;
		this.#importExportBlocker = true;
		document.getElementById('import-export-notification-text').innerText = 'Exporting ...';
		swd.toggleNotification('import-export-notification');
		var fileName = 'unknown.json'
		fetch(url, { signal: AbortSignal.timeout(120000) })
		
		.then(result => {
	        const disposition = result.headers.get('Content-Disposition');
	        fileName = disposition.split(/;(.+)/)[1].split(/=(.+)/)[1];
	        if (fileName.toLowerCase().startsWith("utf-8''"))
	            fileName = decodeURIComponent(fileName.replace(/utf-8''/i, ''));
	        else
	            fileName = fileName.replace(/['"]/g, '');
	        return result.blob();
	    })
	    .then(blob => {
	        const url = window.URL.createObjectURL(blob);
	        const link = document.createElement('a');
	        link.href = url;
	        link.download = fileName;
	        document.body.appendChild(link);
	        link.click();
	        link.remove();
	        swd.toggleNotification('import-export-notification');
	        this.#importExportBlocker = false;
	    });
	}
	
	/*******************/
	/* Authorization   */
	/*******************/
	
	#labelErrorPassswordDontMatch;
	
	initializePasswordFields(password1, password2) {
		this.#labelErrorPassswordDontMatch = document.getElementById('label-error-different-passwords').innerText;
		password1.addEventListener('input', event => {
			if (event.target.value.length === 0) return;
			const inputEvent = document.createEvent('Event');
	        inputEvent.initEvent('input', true, false);
	        password2.dispatchEvent(inputEvent);
		});
		password2.addEventListener('input', event => { 
			if (event.target.value.length === 0) return;
			if (password1.value !== password2.value) password2.setCustomValidity(this.#labelErrorPassswordDontMatch);
			else password2.setCustomValidity('');
		});
	}
	
	/*******************/
	/* Dropdown        */
	/*******************/
	
	initializeLocalizedInputDropdown(dropdown) {
		const content = dropdown.getElementsByClassName('dropdown-content')[0];
		const valueElements = content.getElementsByTagName('a');
		for (const valueElement of valueElements) {
			valueElement.addEventListener('click', event => { 
				const input = dropdown.getElementsByTagName('input')[1];
				if (input) input.value = event.target.innerText;
			});
		}
	}
	
}

const pis = new PIS();

document.addEventListener('readystatechange', event => { 
    if (event.target.readyState === 'interactive') {
		Array.from(document.getElementsByClassName('dropdown'))
			.filter(dropdown => dropdown.classList.contains('input'))
			.forEach(dropdown => pis.initializeLocalizedInputDropdown(dropdown));
	}
});