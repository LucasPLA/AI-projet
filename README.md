# AI-projet

Ce projet a été réalisé par Christophe POLLET et Lucas PLANÇON dans le cadre du projet de 3e/4e année d'IA. L'objectif était ici de développer une IA la plus performante possible sur le jeu de notre choix.

## Le jeu

Pour ce projet, nous avons choisi de travailler sur le jeu de plateau [marrakech](https://boardgamegeek.com/boardgame/29223/marrakech) et ceci pour plusieurs raisons :
 - Les règles du jeu sont très simples ([une vidéo des règles ici](https://www.youtube.com/watch?v=HRiBzxqTS1g))
 - Le jeu est (en théorie) très simple à développer
 - Le jeu présente une certaine part de hasard, ce qui est une contrainte que nous voulions nous donner en plus pour le projet
 
## Le développement du jeu

Pour le développement du projet, nous avons choisi le language java car nous le connaissions bien, avec Maven comme gestionnaire de dépendance.
Bien que très simple d'apparence, algorithmiquement le jeu présente certaines difficultés et développer une première version fonctionnelle n'a pas été trivial. Un certain temps a été consacré là-dessus.
De ce fait, nous avons fait l'impasse sur une interface graphique et préféré un affichage en console.

Afin d'avoir un projet fiable sur lequel pourrait reposer notre IA, nous avons porté une attention particulière à la propreté du code.
Nous avons notamment procédé à des tests unitaires, tests de mutation (Pitest) et à une analyse statique du code (SpotBugs et IntelliJ)

## L'IA

Pour notre IA, nous avons choisi de partir sur un MCTS pour les raisons suivantes :
 - L'algorithme est relativement simple à implémenter
 - Le jeu s'y prête bien : on peut aisément simuler un nombre important de parties ce qui fait la force d'un MCTS
 - Le jeu possède une part de chance et le MCTS se prête bien à la résolution de ce genre de problème

Nous avons codé notre première version à partir d'un code trouvé sur internet.
À première vu, notre IA peut faire au moins 200 000 parties en 10 secondes, ce qui est largement suffisant pour un MCTS.
Seulement, il s'est trouvé un dilemme sur le fait que chaque joueur doit réaliser deux actions : d'abord choisir l'orientation du vendeur, puis choisir la position du tapis à poser.
Dans un premier temps, notre IA ne gérait que le choix d'orientation puis posait un tapis aléatoirement.

Nous avons ensuite entamé une seconde version capable de prendre en considération le choix d'orientation et la pose du tapis. Deux options nous étaient offertes :
- soit partir sur deux MCTS (un pour le choix d'orientation et un pour la pose de tapis). L'inconvénient, c'est que la séparation des deux choix peut conduire à des décisions biaisées de la part du MCTS, comme un mauvais déplacement en partant du principe que la pose de tapis qui suit sera optimale.
- soit partir sur un seul MCTS comprenant toutes les options possibles : les choix seront plus pertinents mais cela augmente très fortement le facteur de branchement et diminue donc le nombre de parties que le MCTS peut simuler, hors c'est la force de l'algorithme.
Nous avons fait le choix de partir sur la deuxième solution car plus efficace et parce que nous pensons que le nombre de parties resterait suffisant.

Malgré cette nouvelle implémentation, les résultats de l'IA restait très moyens.
L'une des causes était que nous ne sanctionnions pas les défaites (nous retournions 0 au lieu de -1 si la partie simulée conduisait à une défaite).
Cela posait problème dans les cas où l'arbre des possibilités d'action était déséquilibré (par exemple lorsque le vendeur est proche d'un mur. Après certains déplacements, il est possible de poser plus de tapis qu'après d'autres et donc certains déplacements conduisent à plus de possibilités (et donc à plus de parties gagnées).
Sans sanction, les déplacements avec le plus de possibilités étaient privilégiés.
Rendre -1 au lieu de 0 en cas de défaite a corrigé ce défaut et a nettement amélioré les performances de notre IA.

## Qualification de l'IA

Notre IA est globalement meilleur qu'un algorithme qui joue toutes les actions au hasard. Elle présente donc au moins une certaine intelligence. Mais étant donnée qu'il y a aussi une part de hasard et de chance dans le jeu, l'IA peut perdre contre des coups aléatoires.

En tant que joueur, les coups qu'elle joue semblent pertinents mais il est toujours difficile de dire s'ils sont optimaux (sinon, nous n'aurions pas besoin d'IA ...).
Le fait que le jeu possède une certaine part de hasard rend plus difficile la quantification du niveau d'une IA. Néanmoins sur beaucoup de parties nous sommes en mesure de dire si une IA est meilleure qu'une autre et notre dernière IA bat nos premières IA plus primitives.

## Bugs connus

Une Null pointer exception peut advenir dans l'IA du MCTS. Il est difficile de dire d'où elle vient puisque ça arrive moins d'une fois toutes les 1 000 000 parties.

Il existe un problème au niveau de l'argent des joueurs qui augmente après la simulation de parties du MCTS. Le bug a été corrigé par une rustine.
