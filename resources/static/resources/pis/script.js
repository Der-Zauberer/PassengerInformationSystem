class PIS {
	
	/*******************/
	/* page            */
	/*******************/
	
	page(page) {
		let currentUrl = window.location.href;
		currentUrl = this.#setParameter(currentUrl, 'page', page);
		window.location.href = currentUrl;
	}
	
	/*******************/
	/* search          */
	/*******************/
	
	search() {
		const input = document.getElementById('search');
		let currentUrl = window.location.href;
		const search = encodeURIComponent(input.value)
			.replace('~', '%7E')
			.replace('!', '%21')
			.replace('*', '%2A')
			.replace('(', '%28')
			.replace(')', '%29');
		currentUrl = this.#removeParameter(currentUrl, 'page');
		currentUrl = this.#removeParameter(currentUrl, 'pageSize');
		currentUrl = input.value ? this.#setParameter(currentUrl, 'search', search) : this.#removeParameter(currentUrl, 'search');
		window.location.href = currentUrl;
	}
	
	resetSearch() {
		let currentUrl = window.location.href;
		currentUrl = this.#removeParameter(currentUrl, 'search');
		currentUrl = this.#removeParameter(currentUrl, 'page');
		currentUrl = this.#removeParameter(currentUrl, 'pageSize');
		window.location.href = currentUrl;
	}
	
	/*******************/
	/* forms           */
	/*******************/
	
	initializeLeaveChecker() {
		window.onhashchange = (event) => this.#checkForDirtyForm();
		window.onbeforeunload = (event) => this.#checkForDirtyForm();
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
	/* import / export */
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
	/* parameter       */
	/*******************/

    #setParameter(url, param, value) {
        let currentUrl = url;
		if (currentUrl.includes('?')) {
			const parameterString = '?' + currentUrl.split('?')[1];
			if (parameterString.includes('?' + param) || parameterString.includes('&' + param)) {
				currentUrl = currentUrl.replace(new RegExp('((\\?|&)' + param + ')(=[^&]*)?', 'gm'), '$1=' + value);
			} else {
                if (currentUrl.substring(currentUrl.length - 1) != '?' && currentUrl.substring(currentUrl.length - 1) != '&') currentUrl += '&';
				currentUrl += param + '=' + value;
			}
		} else {
			currentUrl += '?' + param + '=' + value;
		}	
		return currentUrl;
        
    }

    #removeParameter(url, param) {
        let currentUrl = url;
		if (currentUrl.includes('?')) {
			const parameterString = '?' + currentUrl.split('?')[1];
			if (parameterString.includes('?' + param) || parameterString.includes('&' + param)) {
				currentUrl = currentUrl.replace(new RegExp('&?' + param + '((=[^&]*)|(?=&)|(?=$))', 'gm'), '');
                if (currentUrl.substring(currentUrl.length - 1) == '&') currentUrl = currentUrl.substring(0, currentUrl.length - 1);
                currentUrl = currentUrl.replace('?&', '?');
                if (currentUrl.substring(currentUrl.length - 1) == '?') currentUrl = currentUrl.substring(0, currentUrl.length - 1);
			}
		}
		return currentUrl;
    }

    #getParameter(param) {
        return new URLSearchParams(window.location.search).get(param);
    }
	
}

const pis = new PIS();