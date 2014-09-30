# QA

a small prototype for an Arabic Question answering web application depends on Google Search, analyzing content and respond with the proper answer to some basic questions.

## How does it work ? 

* you give it a question
* it search google with that question
* try to understand the question and what is the answer type ? (name, place, organization..etc)
* get google results and crawl pages
* scrab pages contents and analyse them to get the entities that could be an answer
* decide what is a proper answer from the entities statistics and type of each of them.

## How does this app decide the right answer ?

if you are trying to ask a question such as "What is the Capital of france?", it will understand the you are try to ask about a place entity type, it search google gets some results and scrab paragraphs from them, gets all the places mentioned there and sort them by number of occurrences then remove any entity mentioned in the question in that case "france", then return the place with the highest occurrence, there is a high probability it will be "paris", 
 
## Languages supported

* Arabic

## How does it identify entities ?

it depends on arabic corpus and some other identification rules hard coded in the project.