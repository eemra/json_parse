/**
 * 
 */
package demo.json;

import java.io.File;
import java.io.FileNotFoundException;
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
 *
 */
public class Parser {

	private static String FILE_NAME = "src/main/resources/book_data.json";
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

	/**
	 * 
	 */
	public Parser() {
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
				FILE_NAME = file.getAbsolutePath();
			}
		}
		

		parser.prepareOutputFolderStructure();
		parser.parseAndValidateJSON();

	}

	private void usage() {
		System.out.println("USAGE:");
		System.out.println("\tjava -jar jsonParser.jar [json_file_path]");
		System.out.println();
		System.out.println("\tSamples:");
		System.out.println();
		System.out.println("\tjava -jar jsonParser.jar book_data.json");
	}

	private void prepareOutputFolderStructure() {

		if (OUTPUT_FOLDER.exists() == false) {
			if (OUTPUT_FOLDER.mkdir() == false) {
				System.err.println(OUTPUT_FOLDER.getAbsolutePath() + " can not be created!");
				System.exit(0);
			}
		}

		if (FILE_VALIDATION_ERROR_AUTHOR.exists()) {
			if (FILE_VALIDATION_ERROR_AUTHOR.delete() == false) {
				System.err.println(FILE_VALIDATION_ERROR_AUTHOR.getAbsolutePath() + " can not be deleted!");
				System.exit(0);
			} else {
				System.out.println(FILE_VALIDATION_ERROR_AUTHOR.getAbsolutePath() + " is deleted!");
			}
		}

		if (FILE_VALIDATION_ERROR_BOOK.exists()) {
			if (FILE_VALIDATION_ERROR_BOOK.delete() == false) {
				System.err.println(FILE_VALIDATION_ERROR_BOOK.getAbsolutePath() + " can not be deleted!");
				System.exit(0);
			} else {
				System.out.println(FILE_VALIDATION_ERROR_BOOK.getAbsolutePath() + " is deleted!");
			}
		}

		if (FILE_AUTHORS_HAS_NO_BOOK.exists()) {
			if (FILE_AUTHORS_HAS_NO_BOOK.delete() == false) {
				System.err.println(FILE_AUTHORS_HAS_NO_BOOK.getAbsolutePath() + " can not be deleted!");
				System.exit(0);
			} else {
				System.out.println(FILE_AUTHORS_HAS_NO_BOOK.getAbsolutePath() + " is deleted!");
			}
		}

		if (FILE_BOOK_HAS_INVALID_AUTHOR.exists()) {
			if (FILE_BOOK_HAS_INVALID_AUTHOR.delete() == false) {
				System.err.println(FILE_BOOK_HAS_INVALID_AUTHOR.getAbsolutePath() + " can not be deleted!");
				System.exit(0);
			} else {
				System.out.println(FILE_BOOK_HAS_INVALID_AUTHOR.getAbsolutePath() + " is deleted!");
			}
		}

		if (FILE_MANY_RECORDED_AUTHORS.exists()) {
			if (FILE_MANY_RECORDED_AUTHORS.delete() == false) {
				System.err.println(FILE_MANY_RECORDED_AUTHORS.getAbsolutePath() + " can not be deleted!");
				System.exit(0);
			} else {
				System.out.println(FILE_MANY_RECORDED_AUTHORS.getAbsolutePath() + " is deleted!");
			}
		}

		if (FILE_MANY_RECORDED_BOOKS.exists()) {
			if (FILE_MANY_RECORDED_BOOKS.delete() == false) {
				System.err.println(FILE_MANY_RECORDED_BOOKS.getAbsolutePath() + " can not be deleted!");
				System.exit(0);
			} else {
				System.out.println(FILE_MANY_RECORDED_BOOKS.getAbsolutePath() + " is deleted!");
			}
		}

	}

	/**
	 * 
	 */
	private void parseAndValidateJSON() {
		try {
			System.out.println();
			System.out.println("Started at " + Calendar.getInstance().getTime());
			System.out.println();

			List<Book> bookList = parseBooks();
			List<Author> authorList = parseAuthors();

			Map<String, List<Book>> bookId2BookListMap = new HashMap<String, List<Book>>();
			Map<String, List<Author>> authorId2AuthorListMap = new HashMap<String, List<Author>>();
			Map<String, List<Book>> authorId2BookListMap = new HashMap<String, List<Book>>();

			Map<String, List<Book>> invalidAuthorId2BookMap = new HashMap<String, List<Book>>();
			Map<String, List<Author>> authorHasNoBookMap = new HashMap<String, List<Author>>();

			if (bookList != null && bookList.size() > 0) {
				System.out.println("Book Count in JSON File: " + bookList.size());

				int index = 0;
				for (Book book : bookList) {
					if (book == null) {
						System.err.println("Book is null at " + index + " index");
						continue;
					}

					if (book.getId() == null || book.getName() == null || book.getAuthor() == null) {
						FileUtility.write(FILE_VALIDATION_ERROR_BOOK, book.toString(), true, true);
					}

					if (book.getId() != null) {
						List<Book> books = bookId2BookListMap.get(book.getId());
						if (books == null) {
							books = new ArrayList<Book>();
						}
						books.add(book);

						bookId2BookListMap.put(book.getId(), books);
					}

					if (book.getAuthor() != null) {
						List<Book> books = authorId2BookListMap.get(book.getAuthor());
						if (books == null) {
							books = new ArrayList<Book>();
						}
						books.add(book);

						authorId2BookListMap.put(book.getAuthor(), books);
					}

					index++;
				}

				if (FILE_VALIDATION_ERROR_BOOK.exists()) {
					System.out.println(
							"Invalid book records can be found in " + FILE_VALIDATION_ERROR_BOOK.getAbsolutePath());
				}
			} else {
				System.err.println("No any Book record!");
			}

			if (authorList != null && authorList.size() > 0) {
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

			} else {
				System.err.println("No any Author record!");
			}

			System.out.println("Total Unique Book ID Count: " + bookId2BookListMap.size());
			System.out.println("Total Unique Author ID Count in Author List: " + authorId2AuthorListMap.size());
			System.out.println("Total Unique Author ID Count in Book List: " + authorId2BookListMap.size());

			// invalid author
			List<String> invalidAuthorList = new ArrayList<String>(authorId2BookListMap.keySet());
			invalidAuthorList.removeAll(authorId2AuthorListMap.keySet());

			if (invalidAuthorList.size() > 0) {
				System.out.println("Total Invalid Author ID Count: " + invalidAuthorList.size());
				for (String authorId : invalidAuthorList) {
					invalidAuthorId2BookMap.put(authorId, authorId2BookListMap.get(authorId));
				}

				if (invalidAuthorId2BookMap.size() > 0) {
					System.out.println(
							"Invalid Authors can be found in " + FILE_BOOK_HAS_INVALID_AUTHOR.getAbsolutePath());

					for (List<Book> list : invalidAuthorId2BookMap.values()) {
						FileUtility.write(FILE_BOOK_HAS_INVALID_AUTHOR, toStringBookList(list), true, true);
					}
				}
			} else {
				System.out.println("No Invalid Author ID in Books");
			}

			// author has no book.
			List<String> authorHasNoBookList = new ArrayList<String>(authorId2AuthorListMap.keySet());
			authorHasNoBookList.removeAll(authorId2BookListMap.keySet());

			if (authorHasNoBookList.size() > 0) {
				System.out
						.println("Count of the ID of The Authors that have not a book: " + authorHasNoBookList.size());

				for (String authorId : authorHasNoBookList) {
					authorHasNoBookMap.put(authorId, authorId2AuthorListMap.get(authorId));
				}
				if (authorHasNoBookMap.size() > 0) {
					System.out.println("The Authors that have not a book, can be found in "
							+ FILE_AUTHORS_HAS_NO_BOOK.getAbsolutePath());

					for (List<Author> list : authorHasNoBookMap.values()) {
						FileUtility.write(FILE_AUTHORS_HAS_NO_BOOK, toStringAuthorList(list), true, true);
					}
				}
			} else {
				System.out.println("All Authors that are in Author List, have book(s)");
			}

			// many recorded books
			if (bookId2BookListMap.size() > 0) {
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

			// many recorded authors
			if (authorId2AuthorListMap.size() > 0) {
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			System.out.println();
			System.out.println("Finished at " + Calendar.getInstance().getTime());
			System.out.println();
		}
	}

	/**
	 * it parse the JSON text and reads
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	private List<Book> parseBooks() throws FileNotFoundException, IOException, ParseException {
		String asJson = getAsJSonString("books");

		JsonReader jsonReader = new JsonReader(new StringReader(asJson));

		JsonElement json = new JsonParser().parse(jsonReader);

		Gson gson = new Gson();
		Type collectionType = new TypeToken<List<Book>>() {
		}.getType();

		List<Book> bookList = gson.fromJson(json, collectionType);
		// for (Book book : bookList) {
		// System.out.println(book.toString());
		// }

		return bookList;
	}

	/**
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	private List<Author> parseAuthors() throws FileNotFoundException, IOException, ParseException {
		String asJson = getAsJSonString("authors");

		JsonReader jsonReader = new JsonReader(new StringReader(asJson));

		JsonElement json = new JsonParser().parse(jsonReader);

		Gson gson = new Gson();
		Type collectionType = new TypeToken<List<Author>>() {
		}.getType();

		List<Author> authorList = gson.fromJson(json, collectionType);
		// for (Author author : authorList) {
		// System.out.println(author.toString());
		// }

		return authorList;
	}

	/**
	 * @param objectName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	private String getAsJSonString(String objectName) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(FILE_NAME));

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

		if (list == null || list.size() == 0) {
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

		if (list == null || list.size() == 0) {
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

	// protected String toString(List<Author> authorList) {
	//
	// final String delimiter = "|";
	//
	// if (authorList == null || authorList.size() == 0) {
	// return "";
	// }
	//
	// StringBuilder sb = new StringBuilder();
	//
	// sb.append(authorList.size());
	// sb.append(delimiter);
	//
	// for (Author book : authorList) {
	// if (book == null) {
	// continue;
	// }
	//
	// sb.append(delimiter);
	// sb.append(book.toString());
	// }
	//
	// return sb.toString();
	// }
}
