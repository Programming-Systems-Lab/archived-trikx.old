
function preload(imgObj,imgSrc) {
	if (document.images) {
		eval(imgObj+' = new Image()')
		eval(imgObj+'.src = "'+imgSrc+'"')
	}
}
function changeImage(layer,imgName,imgObj) {
	if (document.images) {
		if (document.layers && layer!=null) eval('document.'+layer+'.document.images["'+imgName+'"].src = '+imgObj+'.src')
		else document.images[imgName].src = eval(imgObj+".src")
	}
}
