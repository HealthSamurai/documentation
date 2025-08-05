# Mathematical Details

See [fastlink](https://imai.fas.harvard.edu/research/files/linkage.pdf) paper for more details.

The algorithm is based on comparisons.

We will use the term _record_ instead of resource here (this term is used in the record linkage articles).

Define a set of comparison functions over pairs of records. Each comparison function returns a single category like

* null
* significantly different
* slightly different
* exactly equal

Different comparison functions can have different sets of possible categories (i.e. codomains are not neccessary equal).

Example of comparison function is

* \-1, if the surname of one of the records is missing
* 0, if levenshtein distance beteween surnames is greater than 2
* 1, if levenshtein distance is 2
* 2, if levenshtein distance is 1
* 3, if surnames are equal

We will say that two records match if they belong to the same entity. For example there can be two records for a single patient. These records can differ. E.g. patient changed name.

We are going to use Bayes theorem. Prior probability is the probability that two random records match.

Then we define two conditional probabilities for each comparison function value:

* m-probability: probability of the specific comparison function value, given that records match
* u-probability: probability of the specific comparison function value, given that records don't match

Then m-probability divided by u-probability is a Bayes factor.

To calculate match score, multiply Bayes factors of each comparison result, and multiply that value by the prior.

To estimate probability, compute x/(1+x), where x is the score. Or calculate it using Bayes theorem.

Note that comparison functions have to be mutually independent. However in practice the algorithm is quite robust to independency violation.

Probability estimation is done using the EM algorithm. It is discussed in detail in the fastlink paper.
