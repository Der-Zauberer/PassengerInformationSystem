class PIS {
	
	page(page) {
		let currentUrl = window.location.href;
		currentUrl = this.#setParameter(currentUrl, 'page', page);
		window.location.href = currentUrl;
	}
	
	search() {
		const input = document.getElementById('search');
		let currentUrl = window.location.href;
		currentUrl = this.#removeParameter(currentUrl, 'page');
		currentUrl = this.#removeParameter(currentUrl, 'pageSize');
		currentUrl = input.value ? this.#setParameter(currentUrl, 'query', input.value) : this.#removeParameter(currentUrl, 'query');
		window.location.href = currentUrl;
	}
	
	resetSearch() {
		let currentUrl = window.location.href;
		currentUrl = this.#removeParameter(currentUrl, 'query');
		currentUrl = this.#removeParameter(currentUrl, 'page');
		currentUrl = this.#removeParameter(currentUrl, 'pageSize');
		window.location.href = currentUrl;
	}

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