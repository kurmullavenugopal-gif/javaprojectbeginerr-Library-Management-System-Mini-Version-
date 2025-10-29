import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

// ------------------- Book Class -------------------
class Book {
    private int id;
    private String title;
    private String author;
    private boolean isBorrowed;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isBorrowed() { return isBorrowed; }

    public LocalDate getDueDate() { return dueDate; }

    public void borrowBook() {
        if (!isBorrowed) {
            isBorrowed = true;
            borrowDate = LocalDate.now();
            dueDate = borrowDate.plusDays(7); // 7-day return policy
            System.out.println(title + " has been borrowed. Due date: " + dueDate);
        } else {
            System.out.println(title + " is already borrowed.");
        }
    }

    public void returnBook() {
        if (isBorrowed) {
            isBorrowed = false;
            LocalDate returnDate = LocalDate.now();
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            if (daysLate > 0) {
                long fine = daysLate * 5; // ₹5 fine per extra day
                System.out.println("You are " + daysLate + " day(s) late. Fine = ₹" + fine);
            } else {
                System.out.println("Returned on time. No fine!");
            }
            borrowDate = null;
            dueDate = null;
            System.out.println(title + " has been returned successfully.");
        } else {
            System.out.println(title + " was not borrowed.");
        }
    }

    @Override
    public String toString() {
        return id + ". " + title + " by " + author + (isBorrowed ? " [Borrowed]" : " [Available]");
    }
}

// ------------------- Member Class -------------------
class Member {
    private String name;
    private ArrayList<Book> borrowedBooks;

    public Member(String name) {
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getName() { return name; }

    public void borrowBook(Book book) {
        if (!book.isBorrowed()) {
            book.borrowBook();
            borrowedBooks.add(book);
        } else {
            System.out.println("Sorry, " + book.getTitle() + " is not available right now.");
        }
    }

    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            book.returnBook();
            borrowedBooks.remove(book);
        } else {
            System.out.println("You didn’t borrow this book.");
        }
    }

    public void showBorrowedBooks() {
        System.out.println("\nBooks borrowed by " + name + ":");
        if (borrowedBooks.isEmpty()) {
            System.out.println("No books borrowed.");
        } else {
            for (Book b : borrowedBooks) {
                System.out.println("- " + b.getTitle() + " (Due: " + b.getDueDate() + ")");
            }
        }
    }
}

// ------------------- Library Class -------------------
class Library {
    private ArrayList<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void showAllBooks() {
        System.out.println("\nLibrary Book List:");
        for (Book b : books) {
            System.out.println(b);
        }
    }

    public Book findBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id)
                return b;
        }
        return null;
    }
}

// ------------------- Main Class -------------------
public class LibrarySystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();
        Member member = new Member("K. Venu Gopal");

        // Sample Books
        library.addBook(new Book(1, "The Alchemist", "Paulo Coelho"));
        library.addBook(new Book(2, "1984", "George Orwell"));
        library.addBook(new Book(3, "Clean Code", "Robert C. Martin"));
        library.addBook(new Book(4, "Rich Dad Poor Dad", "Robert Kiyosaki"));

        int choice;
        do {
            System.out.println("\n=== LIBRARY MENU ===");
            System.out.println("1. Show all books");
            System.out.println("2. Borrow a book");
            System.out.println("3. Return a book");
            System.out.println("4. View borrowed books");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    library.showAllBooks();
                    break;

                case 2:
                    System.out.print("Enter book ID to borrow: ");
                    int borrowId = sc.nextInt();
                    Book borrowBook = library.findBookById(borrowId);
                    if (borrowBook != null)
                        member.borrowBook(borrowBook);
                    else
                        System.out.println("Book not found!");
                    break;

                case 3:
                    System.out.print("Enter book ID to return: ");
                    int returnId = sc.nextInt();
                    Book returnBook = library.findBookById(returnId);
                    if (returnBook != null)
                        member.returnBook(returnBook);
                    else
                        System.out.println("Book not found!");
                    break;

                case 4:
                    member.showBorrowedBooks();
                    break;

                case 5:
                    System.out.println("Thank you for visiting the library!");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 5);

        sc.close();
    }
}
