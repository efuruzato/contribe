Dear Contribe Senior Java Evaluator,

This is just a simple maven project. In order to compile it, run "mvn clean install" and deploy the war file in tomcat. 
Use the contribe.sql file to generate the mysql database, table, user and grant permissions to user.
Here comes some details of the project according to the requirements.

1) While browsing the store, the following information about the store shall be available; title, author and price.

2) Load the initial data from the following link: Bookstoredata
   The data shall only be downloaded, no upload is intended to work.
   >>  Execute com.contribe.bookstore.applications.DownloadBookStock to download the stock and insert items in the database.

3) The class that shall handle the books shall implement the following interface:
      public interface BookList {  
          public Book[] list(String searchString);
          public boolean add(Book book, int quantity);
          public int[] buy(Book... books);
      }
   >>  Please see interface com.contribute.bookstore.service.BookList

4) Status from buy shall provide these statuses: OK(0), NOT_IN_STOCK(1), DOES_NOT_EXIST(2)
   >>  Please see the class com.contribute.bookstore.service.BookListImpl
   
5) The class “Book” shall contain the following variables:
       public class Book {
          private String title;
          private String author;
          private BigDecimal price;
       }
   >> Please see class com.contribe.bookstore.model.Book
   
6) The user shall be able to list books, either the entire stock or by searching title/author.
  >> There are two methods that implement the search: a full text search with Lucene and a simple
  hql search with hql. The search is best experienced in the web interface.
  
7) Furthermore it shall be possible for the user to add and remove books from their basket(where the total price will be available).
  >> Both options (add and remove books) are available in the web and console interfaces.
    
8) It shall be possible to expand the bookstore with new items.
  >> The basket deals with a superclass of Book called ShoppingItem. Any class extending ShoppingItem can be used in the basket.
  
9) Please provide suitable unit tests, where you find it is needed. 
  >> Please lower the value of log4j to INFO before executing the unit tests in  test/main/java.
 
10) Focus on the backend side, most likely there will be another frontend that shall be connected against your backend logic. 
Please use an IDE i.e. Eclipse or netbeans.
  >> A simple web front end is provided. 
     The web store should start in http://localhost:<your port>/contribe.
   
11) Third-party software is allowed, if you can motivate the need. 
If any third-party software is used make sure to bring all the components needed to run the project.
  >> Lucene, log4j, hibernate were the third-party software used. The motivation for Lucene is full text search,
  which works well in searches in a great quantity of books at several fields. Log4j is a simple tool for logging.
  Hibernate is the ORM Object Relational Mapping framework that inspired the specification of JPA and is
  one of the main implementations of JPA.

12) It must be possible to interact against the store for instance via a command line. 
It is not acceptable to only be able to access the store through GET/POST-Requests. 
  >> Execute com.contribe.bookstore.applications.Main for the console interface of the program. 
     Please raise the value of log4j to WARN in log4j.properties before executing the console interface. 


Regards,

Eduardo Furuzato
