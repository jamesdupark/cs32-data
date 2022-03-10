# README

## Section 2: Overall Description
### 2.1: User Needs
Based on the provided data on student stakeholders, our users will need to be able to find compatible group mates based on their provided data and our recommender system. Currently, groupmaking is by word of mouth only or completely random,
so students who don’t have friends in CS32 or just want to meet new people may be matched with incompatible group mates 
and suffer as a result.

Based on our conducted research, there is definitely interest over a group recommendation system. Users want to find 
students who are similar and compatible with them, which is something that our recommender system will accomplish. 
However, some users have expressed concerns about their personal information being collected and used in the algorithm, 
believing that not only is this step unnecessary in the first place, but it may also lead to subjective biases in 
determining compatibility.

Moreover, certain users value the importance of the recommendation system higher than others. For example, user X 
(pseudonym to preserve anonymity of our research clients) is a member of an underrepresented, minority group and has 
difficulty in forming groups where they would feel comfortable. In essence, the recommendation system that we are 
building has different levels of priority to our users.

Users will use this product once by filling out their logistical, technical, and personal information. Then, after all 
users input their information, the program would recommend group matching and the users would then again access the 
program to receive their recommendation. Student users would use this program at any time in their daily life including 
dorm room, classroom, and public study spaces.

Secondary users/stakeholders in this product include the professors and teaching staff of the course, who want to ensure
a relatively smooth semester for all their students. A poorly functioning or harmful recommendation system could lead to
poor class reviews or logistical headaches for the professor.

Our product exacerbates the user need for privacy and data protection, as we collect a lot of (potentially sensitive) 
data on our users. Thus, it is our responsibility to use their data in a safe, secure, and ethical manner.

### 2.2: Assumptions and Dependencies
#### Technical Dependencies
- Our software depends on properly formatted .csv files containing the appropriate student data that we require to make 
  our recommendations
- IntelliJ as an IDE and Java 17 jdk
- Maven as a package manager
- Github for file sharing and collaboration

#### Non-Technical Dependencies
- We are relying on the fact that the data that we are provided were gathered ethically and with informed consent from 
  the student users.
- We are assuming that the data are gathered in line with existing GDPR legalities
- The relevance of our product depends on students taking the class and participating in the group projects/the fact 
  that group project remains core assignment in the course
- We rely on the fact that our data are validated and accurately represent students who are actually taking the course 
  and the fact that their answers to the survey questions apply to them today.
- We rely on the fact that the data are factual and users were not influenced by certain biases, including anchoring and
  adjustment bias, recency bias, convenience bias, etc.

#### Normative Assumptions
- We are prioritizing student users who have provided their information located in the CSV files we will use. Therefore,
  students who forget to provide their information and try to access the recommendations will not benefit much from the 
  matching provided to them, if any.
- We are assuming that students in CS32 require help in project group formations and that there exist students who do 
  not have pre-planned project groups.
- We are relying on the fact that the data that we are provided is representative of what the average user need
- We make normative assumptions about users’ level of access to technology (i.e. internet) and their English proficiency

#### Financial Dependencies
- Continued free access to JetBrain IDEs through Brown
- Funds for replacing personal computers, if needed
- Continued availability of adequately powerful free tiers in services like GCP and Firebase

## Section 3: System Features and Requirements
### 3.1: Risks
#### Stakeholder Risks
- **Unfair outcomes:** we can expect unfair outcomes in certain edge cases, where users are matched with others who 
  don’t align with their interests or values. In addition, due to the fact that various users have different preferences
  for which traits should be used to match them, some users may be given matches that do not align with their desired 
  matching preferences. To do our best to accommodate the various preferences, we have tried to include as many useful 
  features as possible into our matching. Though our algorithm should work as intended, there always exists this case of
  false positive recommendations.
- **Feature representation:** due to restrictions in our matching algorithms, we can only match based on exact string 
  matches between non-numerical traits. Thus, our matching algorithm fails to represent complimentary matches (such as 
  between people with complementary strengths and weaknesses). For similar reasons, traits that were very specific (i.e.
  nationality) were not included in our algorithm as well. In addition, we do not represent features that we did not 
  collect data on, such as personality traits.
- **Data manipulation:** users will not have the option to independently modify or delete their data once it is within 
  our system. This is because the data is already processed inside our system and because our system queries on data 
  existing in the system. If users were to delete their data, then we would potentially have an issue for users who have
  already queried and now their recommended students are no longer in the system.
  We recognize that not having the option to modify or delete user data could be a potential problem for our users. To 
  compromise, we have decided to conduct biweekly polls on the same data for our users. We would then repopulate the 
  entire dataset with this newly collected data, giving the users the opportunity to remove themselves or change some 
  aspect of their data. In special circumstances, users may contact us directly and request that their data be deleted, 
  in which case we will remove their row from the .csv file before loading our recommender system.
- **Disproportionate benefits:** our matching algorithm disproportionately benefits those who prefer to be matched to 
  those similar to them, as it cannot match based on traits that are not similar. In addition, our algorithm benefits 
  those who want to be matched on traits outside of just technical considerations, as we do include traits other than 
  technical considerations in our matching algorithm. However, we do include technical traits as well to try to balance 
  out the recommendations.
- **Publicly accessible data:** No data will be publicly available to users aside from the fact that a given user id is 
  in the database, which when queried upon will return the corresponding user’s name. However, to protect users, we will
  not showcase any other fields as they contain private, personal information about a user.
- **Non-consensual data harvesting:** collected user data will be used to populate our recommender system, but these 
  data were collected with informed consent and in line with both ethical considerations and legal considerations from 
  GDPR guidelines.
- **Blackbox algorithms:** in the data collection process, users will be aware of what information and data we are 
  collecting. From that point, they will only be able to know limited information on what algorithm matching criteria 
  we impose: whether that be disregarding fields completely or weighing various fields differently. This is because we 
  do not want students to realize personal, sensitive information about other users (since every user has access to all 
  other users’ recommendations, a malicious actor could query a given user to pull up a cluster of similar users if they
  knew exactly which traits were being matched on). However, we will make it clear to users that we are not solely 
  matching based on select, sensitive fields such as SSN.
- **Community wellbeing:** we expect our matching system to mostly generate positive results, which will in turn create 
  productive and fun groups. However, some people may not be satisfied with our matching, which could decrease their 
  mental health, social health, motivation, and also create conflict within the class. From this, there could be a 
  distrust of our recommendation system and the overall CS32 course as a whole.
- **Inefficient use of resources:** in order to keep our implementation generic, some of our algorithms require extra 
  passes over a given csv dataset, which may increase the use of computational resources on increasingly large datasets.

#### External Risks
- Our project may contribute to reinforcing existing racial boundaries within computer science, since one of the traits 
  that students are matched on is race. Since our program is unable to match with people who differ on a given trait, 
  we are unable to actively push students to challenge such racial divisions by forming groups with people outside of 
  their racial group.
- Our project is made for students at Brown University, an institution that resides on Native American land and was 
  built using slave labor. In indirectly supporting this institution, we also must recognize the harm that has been 
  done and continues to be done by the institution.

### 3.2: Data Requirements
All data were collected via survey with informed consent.

| Datum name | Storage/Access | Purpose |
| ----------------- | --------------------- | ----------- |
| id | private fields accessible through getters | needed to uniquely id students w/o using identify information such as a name |
| class year | hashed into bloom filter (cannot be extracted) | used to match students using bloom filter |
| race | hashed into bloom filter (cannot be extracted) | used to match students using bloom filter |
| communication style | hashed into bloom filter (cannot be extracted) | used to match students using bloom filter |
| meeting style | hashed into bloom filter (cannot be extracted) | used to match students using bloom filter |
| strengths | hashed into bloom filter (cannot be extracted) | used to match students using bloom filter |
| skills | hashed into bloom filter (cannot be extracted) | used to match students using bloom filter |
| interests | hashed into bloom filter (cannot be extracted) | used to match students using bloom filter |
| weekly available hours | private fields stored in StudentNodes in KDTree and accessible through getAxisVal | needed to find closest euclidean distance between studentNodes |
| years of experience | private fields stored in StudentNodes in KDTree and accessible through getAxisVal | needed to find closest euclidean distance between studentNodes |
| Confidence in SWE skills | private fields stored in StudentNodes in KDTree and accessible through getAxisVal | needed to find closest euclidean distance between studentNodes |


### 3.3: System Features
#### How to build/run the program
In the terminal, input `mvn package` then enter `./run`

The REPL will then execute its run method and the user can input their commands.

We will organize our classes into packages based on what they are used for. Class- and package-specific documentation 
can also be found by running `mvn site` and opening the generated `allpackages-index.html` file in the 
`target/site/apidocs` directory using your web browser.

- `main` package: Contains `Main` and `Repl` class. Parses through terminal inputs and calls the execution of commands. 
In order to make the REPL generic, the REPL only parses through the command by separating it into a list of Strings 
  separated by commas and Strings wrapped in quotations (that can also includes commas). The Repl has a Hashmap<String, 
  REPLCommands> where it retrieve the REPLCommands (in the `Commands` package) value for the first arg key and then 
  calls the execute command method for it.  Also contains all other packages listed below.
- `Blooms` package: Contains the BloomFilter class for general bloom filters, as well as its children 
  (i.e. `StudentBloom`) which are for bloom filters that represent specific objects. Also contains a sub-package 
  `SimilarityMetrics` which contains a `BloomComparator` interface for bloom filter similarity metrics, as well as all 
  implementing classes.
- `Commands` package: Stores interface and classes that associates a command string to a certain method call. This 
  packages allows the REPL to be generic and for engineers to easily add and delete commands for the user to input into 
  the terminal. Also contains a DuplicateCommands exception class for when an engineer attempts to add a command to the 
  hashmap that already exists.
- `CSVParse` package: Stores classes related to the functionality of our generic CSV parser. It handles creating a list 
  of objects indicated by the caller of `CSVParser`. It additionally contains the `Builder` package.
- `Builder` package: Stores interface and classes for the building of a indicated class T. It currently includes 
  Builders for Star, StarNode, StudentNode, and StudentBloomList. The builder creates a new object of the associated 
  class or returns null if building fails.
- `Distances` package: Stores interface and classes for the calculation of distances between two KDNodes.
- `KDimTree` package: Stores classes and exceptions related to KDTrees, including a sub-package for KDNodes with classes
  that implement KDNodes. The exceptions support finding neighbors for the KDTree.
- `KDNodes` package: Stores interface and classes for the nodes on the KDTree. The interface defines a generic node and 
  functionality that enables the tree to be built and queried on. Classes then implement the interface, allowing the 
  tree to be extensible.
- `KNNCalculator` package: Stores interface and classes that calculate the K-nearest neighbors of a given object. Note 
  that Objects being queried must implement KNNComparable.
- `Onboarding` package: Stores the classes used in the onboarding project (Sprint 0). This includes the Star, NightSky, 
  and Coordinate Class. This package parses a CSVFile for Stars and loads it into a list in NightSky. NightSky also 
  finds the k-nearest neighbors for a coordinate or star. The x, y, and z position of the sky is stored in a coordinate 
  class.
#### Backend:
- **Bloom Filter:** provides a fast, one-sided accuracy check of whether a member is in a given set.
- **K-d Tree:** puts multi-dimensional elements into a balanced k-d tree, either through manual insertion or through 
  a list.
- **CSV Parser:** can generically parse through CSV files and create a list of objects defined by
  the caller from CSV lines.

#### Frontend:
- **REPL:** allows for user to input commands and prints results as well as informative error messages upon failure. 
  Also allows for engineers to add commands without editing the main code which allows the REPL to be generic.

### 3.4: Functional Requirements

Our REPL has the following user-facing requirements:

#### NightSky Commands
- `stars <filepath>`: parses through the CSV file and store a list of Star objects from the CSV lines.
- `naive_neighbors <k> <x> <y> <z>`: prints the k-nearest stars to the point located at (x, y, z)
- `naive_neighbors <k> <star name>`: prints the k-nearest stars from an existing star.

#### KD Commands
- `load_kd <filepath>`: reads from a CSV file `filepath` and inserts the entries into a KDTree. The number of entries 
  read and inserted into the tree will then be printed out along with the entered filepath.
- `similar_kd <k> <target_id>`: prints the `k`-nearest neighbors on the KDTree defined on a distance metric to the node 
  corresponding to the `target_id`, excluding the` target_id` node. If two or more nodes are equidistant to the target node, they will then be randomly selected. The order of the nodes printed are in ascending order based on the distance metric.

#### Bloom Filter Commands
- `create_bf <r> <n>`: creates a single bloom filter with desired false positive rate `r` and maximum number of 
  elements `n`
- `insert_bf <element>`: inserts the given `element` into the current bloom filter, if one exists.
- `query_bf <element>`: queries the current bloom filter for the presence of the given `element`
- `load_bf <filepath>`: reads in the .csv file located at `filepath` and creates bloom filters based on each student, 
  storing them in an internal database
- `similar_bf <k> <n>`: queries the current database for the `k` most similar bloom filters to the filter with id `n`, 
  using the `XNOR` similarity metric by default.

In addition to user-facing requirements, we also have the following backend requirements which support the 
user-facing requirements:
#### Bloom Filter
- Calculation of the size and number of hashes associated with a filter based on a given max number of elements and 
  false positive rate.
- Insertion of a given object into the filter’s set, as well as querying for any object’s presence in the set.
- Reading in bloom filters with identical parameters for each student in a properly-formatted .csv file
- Calculating the k-most similar filters to a given filter based on a generic similarity metric, using XNOR similarity 
  by default.
- Engineers are able to create their own bloom filter similarity metric implementing BloomComparator
- Engineers are able to create their own subclasses of `BloomFilter` which store information about specific objects

#### Generic REPL
- Engineers are able to remove and add commands from the REPL. Engineers can add or remove classes that implement 
REPLCommands from the list passed into the Repl class from the main class. REPLCommands stores a set of command 
arguments to recognize from a user input in the terminal. It also calls the execution of the actions associated 
with the command argument 0.

#### Generic CSV Parser
- Engineers are able to use the generic CSV parser by creating a builder class that implements CSVBuilder. They can 
then create a CSVParser object with their builder class as an input. Then, parse method of CSVParser will take in the 
path of the CSV file and store a list of indicated class T made from the lines of the CSV file.

#### K-d Tree
- Insertion of a list of KDNodes into the KDTree that can be either read from a CSV file path or instantiated, ensuring 
that the resulting tree will be balanced.
- Insertion of a single KDNode element into the KDTree, ensuring that the resulting tree will be balanced.
- Ability to insert a node into the tree based on the respective axis value. This enables any k-dimension implementation 
of the tree.
- Ability to balance a tree that is defined on the following metrics: nodes on any given left subtree is less than the 
parent compared on the respective axis in the tree; and, nodes on any given right subtree is greater than or equal to 
the parent compared on the respective axis in the tree.
- Ability to roughly balance a tree by ensuring that the number of nodes on the left subtree should be around the 
number of nodes on the right subtree for any particular node. This is ensured through the sorting of the list compared 
on the specific axis value and obtaining the first element in the middle that is different than its left neighbor.
- Querying for the nearest neighbors defined on any given distance metric.
- Querying for the nearest neighbor where nodes on the KDTree are equidistant to the target node.
- Engineers are able to create their own subclasses of `Distances` that computes the distance between two KDNodes which
  enables extensibility.
- Engineers are able to create their own subclasses of `KDNode` which store information about specific objects and 
  enable the objects to be inserted into the KDTree.

### 3.5: Testing Plan
Mentioned edge cases and tasks are tested through extensive System and Unit Testing.
#### REPL / CSV Parsing
Extensive REPL and CSV Parsing testing has been done in the onboarding test folder with the stars concept. REPL and CSV 
Parsing is also automatically tested through the testing done for KDTree and Bloom Filter.
- [System Testing] Error messages are printed when a command does not exist, there are too few arguments, there are too 
  many arguments.
- [System Testing] Error messages are printed when a CSV file has the wrong header, a line has more entries than the 
  header indicates, a line has less entries than the header indicates, a NumberFormatException occurs when building a 
  class from a line with non-numerical Strings being converted to a int or double.
- [Unit Testing] Builder classes output classes with the expected fields. Error messages are printed and null is 
  returned by build method for when NumberFormatException, too few elements, and too many elements occurs from input 
  list of strings. This is tested for builder for Star, StarNode, and StudentBloomList.

#### Bloom Filter
- [System Testing] creation, insertion, and querying of filters with appropriate sizes based on the given parameters
- [System Testing] appropriate error messages for out-of-bounds parameters (i.e. negative max elements, false positive 
  rate >1, size 0 bitsets, non-integer ids, negative k, etc.)
- [System Testing] reading in the correct number of students as bloom filters using the CSV parser.
- [System Testing] can find the k-most similar neighbors to the student with a given id.
- [System Testing] commands that rely on loading or creating bloom filters using `create_bf` or `load_bf` throw 
  appropriate error messages when called before the command they depend on.
- [Unit Testing] correctly calculates size and number of hashes given false positive rate and max elements
- [Unit Testing] hashing for insertion and querying works as intended, and the corresponding bits are set accordingly
- [Unit Testing] bloom filters with the same parameters can be compared using XNOR or other similarity metrics.
- [Unit Testing] generic CSV Parser can be used to create bloom filters for each object with the same parameters
- [Unit Testing] k-nearest neighbor calculator can find k-nearest neighbors to a given bloom filter in a set, with the 
  given filter not being part of the returned list and ties being broken randomly.


#### KD Tree
- [System Testing] creation, insertion, and querying of both valid and invalid nodes for the KDTree. Valid and invalid 
  nodes consist of improper CSV Headers, improper number of fields for the entries inside the CSV, and loading 
  incorrectly named CSVs. Also covers the case of querying without loading data from a CSV.
- [System Testing] various values for k that are used for finding neighbors, including negative k values, k equals to 
  zero, k equal to the number of nodes in the CSV, and k greater than the number of nodes in the CSV.
- [System Testing] various, basic examples of reading CSV data and then querying off of it, mixed with reloading the 
  same CSV data into the KDTree.
- [Unit Testing] correctly tests whether KDTree inserts properly based on axis dimensions, including specific tests 
  for inserting into root, left subtree, right subtree, and left / right subtree of various depths.
- [Unit Testing] correctly tests for inserting nodes with the same values, nodes with the same values on one or more 
  specific axes, and various different node values. This ensures that the tree is correctly balanced, defined on the 
  premise of nodes on the left subtree being less than the parent and nodes on the right subtree being greater than or 
  equal to the parent.
- [Unit Testing] finding neighbors based on two general premises: one that involves randominizing and one that does 
  not. Randomizing comes into play when there are two nodes that are equidistant to the target node. For randomization, 
  properties of the return list are tested, including checking the number of unique distance(s) in the priority queue, 
  size of the list, and sum of the distances. In the non-randomizing case, two general cases are tested: closest 
  neighbor is on the traversal tree and closest neighbor is not on the traversal tree.

### 3.6: External Interface Requirements
Our external interface will be a REPL on the terminal, accessible by using the `./run` command in the appropriate 
directory. All related commands will be accessible through the REPL. As we are limited in the UIs we can produce 
at the moment, it will be difficult to make our system extremely accessible to all kinds of users at the moment.

We will not be integrating any other external softwares yet.

### 3.7 Non-functional Requirements
- **Performance:** We aim for most operations to be achievable on the timescale of several milliseconds. All in all, 
  our only major performance concern is that users do not notice any delays between the inputting of their commands 
  and their execution.
- **Security:** As some student data we collected are confidential, we aim to ensure that our internal database is not 
  accessible to unauthorized users.
- **Privacy:** Once again, as some student data used are confidential, we must ensure that there is no way to infer 
  confidential information about a user using our system.
- **UI:** Our UI capabilities are extremely limited, so we do not aim to have an especially pleasing UI. Instead, we 
  will aim to have helpful and informative error messages when things go wrong.
