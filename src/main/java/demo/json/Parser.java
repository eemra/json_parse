/**
 *
 */
package demo.json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import demo.json.model.Author;
import demo.json.model.Book;
import demo.json.util.FileUtility;

/**
 * @author resul.avan@gmail.com
 */
public class Parser {

    private static final File OUTPUT_FOLDER = new File("output");

    private static final File FILE_VALIDATION_ERROR_BOOK = new File(
            OUTPUT_FOLDER.getAbsolutePath() + "/ValidationErrorBooks.txt");
    private static final File FILE_VALIDATION_ERROR_AUTHOR = new File(
            OUTPUT_FOLDER.getAbsolutePath() + "/ValidationErrorAuthors.txt");

    private static final File FILE_BOOK_HAS_INVALID_AUTHOR = new File(
            OUTPUT_FOLDER.getAbsolutePath() + "/BooksHasInvalidAuthor.txt");

    private static final File FILE_AUTHORS_HAS_NO_BOOK = new File(
            OUTPUT_FOLDER.getAbsolutePath() + "/AuthorsHasNoBook.txt");

    private static final File FILE_MANY_RECORDED_BOOKS = new File(
            OUTPUT_FOLDER.getAbsolutePath() + "/ManyRecordedBooks.txt");

    private static final File FILE_MANY_RECORDED_AUTHORS = new File(
            OUTPUT_FOLDER.getAbsolutePath() + "/ManyRecordedAuthors.txt");

    private static String fileName = "src/main/resources/book_data.json";

    /**
     *
     */
    public Parser() {
        // constructor
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        Parser parser = new Parser();

        if (args.length > 0) {
            File file = new File(args[0]);
            if (!file.exists()) {
                parser.usage();
                System.out.println();
                System.out.println("\tNo such file " + file.getAbsolutePath());
                System.exit(0);
            } else {
                fileName = file.getAbsolutePath();
            }
        }

        parser.prepareOutputFolderStructure();
        try {
            parser.parseAndValidateJSON();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void usage() {
        System.out.println("USAGE:");
        System.out.println("\tjava -jar jsonParser.jar [json_file_path]");
        System.out.println();
        System.out.println("\tSamples:");
        System.out.println();
        System.out.println("\tjava -jar jsonParser.jar book_data.json");
    }

    private void prepareOutputFolderStructure(File folder) {
        if (folder.exists()) {
            if (!folder.delete()) {
                System.err.println(folder.getAbsolutePath() + " can not be deleted!");
                System.exit(0);
            } else {
                System.out.println(folder.getAbsolutePath() + " is deleted!");
            }
        }
    }

    private void prepareOutputFolderStructure() {
        if (!OUTPUT_FOLDER.exists() && !OUTPUT_FOLDER.mkdir()) {
            System.err.println(OUTPUT_FOLDER.getAbsolutePath() + " can not be created!");
            System.exit(0);
        }

        prepareOutputFolderStructure(FILE_VALIDATION_ERROR_AUTHOR);
        prepareOutputFolderStructure(FILE_VALIDATION_ERROR_BOOK);
        prepareOutputFolderStructure(FILE_AUTHORS_HAS_NO_BOOK);
        prepareOutputFolderStructure(FILE_BOOK_HAS_INVALID_AUTHOR);
        prepareOutputFolderStructure(FILE_MANY_RECORDED_AUTHORS);
        prepareOutputFolderStructure(FILE_MANY_RECORDED_BOOKS);
    }

    /**
     *
     */
    private void parseAndValidateJSON() throws IOException, ParseException {
        System.out.println();
        System.out.println("Started at " + Calendar.getInstance().getTime());
        System.out.println();

        ParseResult parseResult = parse(parseBooks());
        Map<String, List<Author>> authorId2AuthorListMap = getAuthorListMap(parseAuthors());

        System.out.println(String.format("Total Unique Book ID Count: %d%n" +
                        "Total Unique Author ID Count in Author List: %d%n" +
                        "Total Unique Author ID Count in Book List:%d",
                parseResult.bookId2BookListMap.size(),
                authorId2AuthorListMap.size(),
                parseResult.authorId2BookListMap.size()));

        findInvalidAuthors(parseResult.authorId2BookListMap, authorId2AuthorListMap);
        findAuthorsThatHaveNoBook(parseResult.authorId2BookListMap, authorId2AuthorListMap);
        findManyRecordedBooks(parseResult.bookId2BookListMap);
        findManyRecordedAuthors(authorId2AuthorListMap);
    }


    private static ParseResult parse(List<Book> bookList) throws IOException {
        ParseResult parseResult = new ParseResult();

        if (bookList == null || bookList.isEmpty()) {
            System.err.println("No any Book record!");
            return parseResult;
        }
        System.out.println("Book Count in JSON File: " + bookList.size());

        int index = 0;
        for (Book book : bookList) {
            if(!isValid(book,index)){
                continue;
            }

            List<Book> books = parseResult.getBookId2BookListMap().get(book.getId());
            if (books == null) {
                books = new ArrayList<Book>();
            }
            books.add(book);
            parseResult.getBookId2BookListMap().put(book.getId(), books);

            books = parseResult.getAuthorId2BookListMap().get(book.getAuthor());
            if (books == null) {
                books = new ArrayList<Book>();
            }
            books.add(book);
            parseResult.getAuthorId2BookListMap().put(book.getAuthor(), books);

            index++;
        }

        if (FILE_VALIDATION_ERROR_BOOK.exists()) {
            System.out.println(
                    "Invalid book records can be found in " + FILE_VALIDATION_ERROR_BOOK.getAbsolutePath());
        }

        return parseResult;
    }

    private static boolean isValid(Book book,int index) throws IOException{
        if (book == null) {
            System.err.println("Book is null at " + index + " index");
            return false;
        }

        if (book.getId() == null || book.getName() == null || book.getAuthor() == null) {
            FileUtility.write(FILE_VALIDATION_ERROR_BOOK, book.toString(), true, true);
            return false;
        }

        return true;
    }

    private static class ParseResult {

        private Map<String, List<Book>> bookId2BookListMap = new HashMap<String, List<Book>>();
        private Map<String, List<Book>> authorId2BookListMap = new HashMap<String, List<Book>>();

        public ParseResult() {
            // empty
        }

        public Map<String, List<Book>> getBookId2BookListMap() {
            return bookId2BookListMap;
        }

        public void setBookId2BookListMap(Map<String, List<Book>> bookId2BookListMap) {
            this.bookId2BookListMap = bookId2BookListMap;
        }

        public Map<String, List<Book>> getAuthorId2BookListMap() {
            return authorId2BookListMap;
        }

        public void setAuthorId2BookListMap(Map<String, List<Book>> authorId2BookListMap) {
            this.authorId2BookListMap = authorId2BookListMap;
        }
    }

    private Map<String, List<Author>> getAuthorListMap(List<Author> authorList) throws IOException {
        Map<String, List<Author>> authorId2AuthorListMap = new HashMap<String, List<Author>>();
        if (authorList == null || authorList.isEmpty()) {
            System.err.println("No any Author record!");
            return authorId2AuthorListMap;
        }
        System.out.println("Author Count in JSON File: " + authorList.size());

        int index = 0;
        for (Author author : authorList) {
            if (author == null) {
                System.err.println("Author is null at " + index + " index");
                continue;
            }

            if (author.getId() == null || author.getName() == null) {
                FileUtility.write(FILE_VALIDATION_ERROR_AUTHOR, author.toString(), true, true);
            }

            if (author.getId() != null) {
                List<Author> authors = authorId2AuthorListMap.get(author.getId());
                if (authors == null) {
                    authors = new ArrayList<Author>();
                }
                authors.add(author);

                authorId2AuthorListMap.put(author.getId(), authors);
            }
            index++;
        }

        if (FILE_VALIDATION_ERROR_AUTHOR.exists()) {
            System.out.println(
                    "Invalid authpr records can be found in " + FILE_VALIDATION_ERROR_AUTHOR.getAbsolutePath());
        }

        return authorId2AuthorListMap;
    }

    private void findAuthorsThatHaveNoBook(Map<String, List<Book>> authorId2BookListMap, Map<String, List<Author>> authorId2AuthorListMap) throws IOException {
        // author has no book.
        List<String> authorHasNoBookList = new ArrayList<String>(authorId2AuthorListMap.keySet());
        authorHasNoBookList.removeAll(authorId2BookListMap.keySet());

        if (authorHasNoBookList.isEmpty()) {
            System.out.println("All Authors that are in Author List, have book(s)");
            return;
        }

        System.out
                .println("Count of the ID of The Authors that have not a book: " + authorHasNoBookList.size());

        Map<String, List<Author>> authorHasNoBookMap = new HashMap<String, List<Author>>();

        for (String authorId : authorHasNoBookList) {
            authorHasNoBookMap.put(authorId, authorId2AuthorListMap.get(authorId));
        }
        if (authorHasNoBookMap.isEmpty()) {
            return;
        }
        System.out.println("The Authors that have not a book, can be found in "
                + FILE_AUTHORS_HAS_NO_BOOK.getAbsolutePath());

        for (List<Author> list : authorHasNoBookMap.values()) {
            FileUtility.write(FILE_AUTHORS_HAS_NO_BOOK, toStringAuthorList(list), true, true);
        }

    }

    private void findManyRecordedBooks(Map<String, List<Book>> bookId2BookListMap) throws IOException {
        if (bookId2BookListMap.isEmpty()) {
            return;
        }
        int count = 0;
        for (List<Book> list : bookId2BookListMap.values()) {
            if (list != null && list.size() > 1) {
                FileUtility.write(FILE_MANY_RECORDED_BOOKS, toStringBookList(list), true, true);
                count++;
            }
        }

        System.out.println("Many Recorded Book ID in Book List: " + count);

        if (FILE_MANY_RECORDED_BOOKS.exists()) {
            System.out.println(
                    "Many Recorded Book IDs can be found in " + FILE_MANY_RECORDED_BOOKS.getAbsolutePath());
        }
    }

    private void findManyRecordedAuthors(Map<String, List<Author>> authorId2AuthorListMap) throws IOException {
        if (authorId2AuthorListMap.isEmpty()) {
            return;
        }

        int count = 0;
        for (List<Author> list : authorId2AuthorListMap.values()) {

            if (list != null && list.size() > 1) {
                FileUtility.write(FILE_MANY_RECORDED_AUTHORS, toStringAuthorList(list), true, true);
                count++;
            }
        }

        System.out.println("Many Recorded Author ID in Author List: " + count);
        if (FILE_MANY_RECORDED_AUTHORS.exists()) {
            System.out.println(
                    "Many Recorded Author IDs can be found in " + FILE_MANY_RECORDED_AUTHORS.getAbsolutePath());
        }
    }

    private void findInvalidAuthors(Map<String, List<Book>> authorId2BookListMap, Map<String, List<Author>> authorId2AuthorListMap) throws IOException {
        Map<String, List<Book>> invalidAuthorId2BookMap = new HashMap<String, List<Book>>();

        // invalid author
        List<String> invalidAuthorList = new ArrayList<String>(authorId2BookListMap.keySet());
        invalidAuthorList.removeAll(authorId2AuthorListMap.keySet());

        if (invalidAuthorList.isEmpty()) {
            System.out.println("No Invalid Author ID in Books");
            return;
        }

        System.out.println("Total Invalid Author ID Count: " + invalidAuthorList.size());
        for (String authorId : invalidAuthorList) {
            invalidAuthorId2BookMap.put(authorId, authorId2BookListMap.get(authorId));
        }

        if (invalidAuthorId2BookMap.isEmpty()) {
            return;
        }

        System.out.println(
                "Invalid Authors can be found in " + FILE_BOOK_HAS_INVALID_AUTHOR.getAbsolutePath());

        for (List<Book> list : invalidAuthorId2BookMap.values()) {
            FileUtility.write(FILE_BOOK_HAS_INVALID_AUTHOR, toStringBookList(list), true, true);
        }
    }

    /**
     * it parse the JSON text and reads
     *
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private List<Book> parseBooks() throws IOException, ParseException {
        String asJson = getAsJSonString("books");

        JsonReader jsonReader = new JsonReader(new StringReader(asJson));

        JsonElement json = new JsonParser().parse(jsonReader);

        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Book>>() {
        }.getType();

        return gson.fromJson(json, collectionType);
    }

    /**
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private List<Author> parseAuthors() throws IOException, ParseException {
        String asJson = getAsJSonString("authors");

        JsonReader jsonReader = new JsonReader(new StringReader(asJson));

        JsonElement json = new JsonParser().parse(jsonReader);

        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Author>>() {
        }.getType();

        return gson.fromJson(json, collectionType);
    }

    /**
     * @param objectName
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private String getAsJSonString(String objectName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(fileName));

        JSONObject jsonObject = (JSONObject) obj;

        JSONArray userDetJson = (JSONArray) jsonObject.get(objectName);

        return userDetJson.toJSONString();
    }

    /**
     * @param list
     * @return
     */
    private String toStringBookList(List<Book> list) {

        final String delimiter = "|";

        if (list == null || list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(list.size());

        for (Book obj : list) {
            if (obj == null) {
                continue;
            }

            sb.append(delimiter);
            sb.append(obj.toString());
        }

        return sb.toString();
    }

    /**
     * @param list
     * @return
     */
    private String toStringAuthorList(List<Author> list) {

        final String delimiter = "|";

        if (list == null || list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(list.size());

        for (Author obj : list) {
            if (obj == null) {
                continue;
            }

            sb.append(delimiter);
            sb.append(obj.toString());
        }

        return sb.toString();
    }

}
