foldersTree = gFld("<i>Components</i>")

aux1 = insFld(foldersTree, gFld("Kinesthetic eXtreme", "/content/static/tree/kxtree.html"))

aux2 = insFld(aux1, gFld("Event Notifier", "/content/static/tree/eventnotifiertree.html"))

aux2 = insFld(aux1, gFld("Event Distiller", "/content/static/tree/eventdistillertree.html"))

aux2 = insFld(aux1, gFld("Metaparser", "/content/static/tree/metaparsertree.html"))

aux3 = insFld(aux2, gFld("Oracle", "/content/static/tree/oracletree.html")) 

aux1 = insFld(foldersTree, gFld("Worklets", "/content/static/tree/workletstree.html"))

aux1 = insFld(foldersTree, gFld("WGCache", "/content/static/tree/wgcachetree.html"))

