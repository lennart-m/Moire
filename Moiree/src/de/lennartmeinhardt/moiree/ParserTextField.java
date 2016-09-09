package de.lennartmeinhardt.moiree;

import java.util.function.Predicate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.TextField;

/**
 * A simple text field that automatically parses input text.
 * The {@link Parser} property manages parsing.
 * 
 * @author Lennart Meinhardt
 *
 * @param <T> the type of object being parsed from text input
 */
public class ParserTextField <T> extends TextField {
	
	// pseudo classes for parsed (success) and unparsed (error) to use in css
	private static final PseudoClass PSC_PARSED = PseudoClass.getPseudoClass("parsed");
	private static final PseudoClass PSC_UNPARSED = PseudoClass.getPseudoClass("unparsed");

	private final Property<Parser<? extends T>> parserProperty = new SimpleObjectProperty<Parser<? extends T>>(this, "parser") {
		@Override protected void invalidated() {
			parse();
		}
	};

	// the value being automatically parsed
	private final ObjectProperty<T> parsedValueProperty = new SimpleObjectProperty<>();
//	// the state of parsing success
	private final ObjectProperty<ParseState> parseStateProperty = new SimpleObjectProperty<ParseState>(this, "parseState") {
		@Override protected void invalidated() {
			pseudoClassStateChanged(PSC_PARSED, getValue() == ParseState.PARSED);
			pseudoClassStateChanged(PSC_UNPARSED, getValue() == ParseState.UNPARSED);
		};
	};
	
	// can be used to check if the last parse was successful
	private final BooleanProperty parseSuccessfulProperty = new SimpleBooleanProperty();
	
	
	/**
	 * Create a new {@link ParserTextField} with empty text.
	 * Calls the text constructor in order to perform an initial parse.
	 */
	public ParserTextField() {
		this("");
	}
	
	/**
	 * Create a new {@link ParserTextField} with given text.
	 * Performs an initial parse.
	 * 
	 * @param text the text to set
	 */
	public ParserTextField(String text) {
		initialize();
		// first register the listener, then set the text
		setText(text);
	}
	
	private final void initialize() {
		parseSuccessfulProperty.bind(parseStateProperty.isEqualTo(ParseState.PARSED));
		getStyleClass().add("parser-text-field");
		textProperty().addListener(e -> parse());
	}
	

	/**
	 * Do the parsing. Check if the parser throws an exception. If not the value was parsed successfully and can be set.
	 * The pseudo class is held updated.
	 */
	private final void parse() {
		Parser<? extends T> parser = getParser();
		if(parser != null) {
			try {
				T value = parser.parse(getText());
				parseStateProperty.setValue(ParseState.PARSED);
				parsedValueProperty.setValue(value);
			} catch(IllegalArgumentException ex) {
				parseStateProperty.setValue(ParseState.UNPARSED);
			}
		}
	}
	
	public ReadOnlyProperty<T> parsedValueProperty() {
		return parsedValueProperty;
	}
	public T getParsedValue() {
		return parsedValueProperty.getValue();
	}
	
	public Property<Parser<? extends T>> parserProperty() {
		return parserProperty;
	}
	public Parser<? extends T> getParser() {
		return parserProperty.getValue();
	}
	public void setParser(Parser<? extends T> parser) {
		parserProperty.setValue(parser);
	}
	
	public ReadOnlyBooleanProperty parseSuccessfulProperty() {
		return parseSuccessfulProperty;
	}
	public boolean isParseSuccessful() {
		return parseSuccessfulProperty.get();
	}
	
	
	/**
	 * The different parse states.
	 */
	private static enum ParseState {
		PARSED, UNPARSED;
	}
	
	
	/**
	 * Parses an object from a string. If parsing yields an error an {@link IllegalArgumentException} can be thrown. It will be caught by the {@link ParserTextField}.
	 * 
	 * @author Lennart Meinhardt
	 *
	 * @param <T> the type of object that can be parsed
	 */
	@FunctionalInterface
	public static interface Parser <T> {

		// Simple parsers
		public static final Parser<Integer> INT_PARSER = Integer::parseInt;
		public static final Parser<Double> DOUBLE_PARSER = Double::parseDouble;
		public static final Parser<Long> LONG_PARSER = Long::parseLong;
		
		
		/**
		 * Create an object from given input string.
		 * 
		 * @param s the string
		 * @return the parsed object
		 * @throws IllegalArgumentException if the string could not be parsed
		 */
		T parse(String s) throws IllegalArgumentException;
		
		
		/**
		 * Create a new {@link Parser} that also throws an exception if the given predicate returns false.
		 * So values with false result are considered unsuccessful parsed.
		 * 
		 * @param condition the veto object
		 * @return
		 */
		default Parser<T> withVeto(Predicate<? super T> condition) {
			return s -> {
				T value = this.parse(s);
				if(! condition.test(value))
					throw new IllegalArgumentException();
				return value;
			};
		}
	}
}
