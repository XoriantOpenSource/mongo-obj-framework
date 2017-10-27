package org.smof.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.smof.annnotations.SmofFilter;
import org.smof.annnotations.SmofIndexField;
import org.smof.annnotations.SmofPFEQuery;
import org.smof.annnotations.SmofPartialIndex;
import org.smof.annnotations.SmofQueryA;
import org.smof.element.Element;
import org.smof.exception.SmofException;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

public class PartialIndex {
	private final Bson index;
	private final IndexOptions options;
	private final Set<Bson> rawIndexes;
	private static final String UNIQUE = "unique";
	
	public PartialIndex(Set<Bson> indices,IndexOptions options){
		index=fromIndexes(new ArrayList<Bson>(indices));
		this.options=options;
		rawIndexes=indices;
	}
	
	public PartialIndex() {
		index=fromIndexes(new ArrayList<Bson>());
		this.options=null;
		rawIndexes=null;
	}

	public static PartialIndex fromBson(BsonDocument pi){
		return new PartialIndex(createRawIndexes(pi),createOptions(pi));
	}
	private Bson fromIndexes(List<Bson> indexes) {
		if(indexes.size() == 1) {
			return indexes.get(0);
		}
		return Indexes.compoundIndex(indexes);
	}
	public Bson getIndex() {
		return index;
	}

	public IndexOptions getOptions() {
		return options;
	}
	
	
	public static PartialIndex fromSmofIndex(SmofPartialIndex note) {
		return new PartialIndex(createRawIndexes(note.fields()), createOptions(note));
	}
	
	private static IndexOptions createOptions(BsonDocument doc) {
		final IndexOptions options = new IndexOptions();
		options.unique(isUnique(doc));
		return options;
	}
	
	public static IndexOptions createOptions(SmofPartialIndex note) {
		final IndexOptions options = new IndexOptions();
		SmofPFEQuery spq=note.pfe();
		SmofQueryA sqa=spq.expression()[0];
		SmofFilter sf=sqa.query()[0];
		BsonDocument partialFilterExpression=new BsonDocument();
		partialFilterExpression=BsonDocument.parse("{"+spq.name()+":{"+sf.operator().getMongoToken()+":"+sf.value()+"}}");
		System.out.println(partialFilterExpression);
		options.partialFilterExpression(partialFilterExpression);
		return options;
	}
	
	private static boolean isUnique(BsonDocument doc) {
		return doc.containsKey(UNIQUE) && doc.getBoolean(UNIQUE).getValue();
	}
	private static Set<Bson> createRawIndexes(SmofIndexField[] fields) {
		final Set<Bson> indexes = new LinkedHashSet<>();
		if(fields.length == 1) {
			indexes.add(fromSmofIndexField(fields[0]));
		}
		else if(fields.length > 1) {
			indexes.addAll(fromSmofIndexFields(fields));
		}
		return indexes;
	}
	private static List<Bson> fromSmofIndexFields(SmofIndexField[] fields) {
		return Arrays.stream(fields)
				.map(f -> fromSmofIndexField(f))
				.collect(Collectors.toList());
	}
	private static List<Bson> parseIndexName(String name) {
		final StringTokenizer tokens = new StringTokenizer(name, "_");
		final List<Bson> indexes = new ArrayList<>();
		
		while(tokens.hasMoreTokens()) {
			Bson index = nextIndex(tokens);
			indexes.add(index);
		}
		return indexes;
	}
	
	private static Bson nextIndex(StringTokenizer tokens) {
		final Bson index;
		String indexName = tokens.nextToken();
		String indexTypeStr = tokens.nextToken();
		IndexType indexT;
		
		while((indexT = IndexType.parse(indexTypeStr)) == null) {
			indexName+= "_" + indexTypeStr;
			indexTypeStr = tokens.nextToken();
		}
		
		switch(indexT) {
		case ASCENDING:
			index = Indexes.ascending(indexName);
			break;
		case DESCENDING:
			index = Indexes.descending(indexName);
			break;
		case TEXT:
			index = Indexes.text(indexName);
			break;
		default:
			handleError(new IllegalArgumentException("Invalid bson index"));
			index = null;
			break;
		}
		return index;
	}
	private static Set<Bson> createRawIndexes(BsonDocument doc) {
		final Set<Bson> indexes = new LinkedHashSet<>();
		final String name = doc.getString("name").getValue();
		if(name.equals("_id_")) {
			indexes.add(Indexes.ascending(Element.ID));
		}
		else {
			indexes.addAll(parseIndexName(name));
		}
		return indexes;
	}
	private static Bson fromSmofIndexField(SmofIndexField smofIndexField) {
		final String fieldName = smofIndexField.name();
		switch(smofIndexField.type()) {
		case ASCENDING:
			return Indexes.ascending(fieldName);
		case DESCENDING:
			return Indexes.descending(fieldName);
		case TEXT:
			return Indexes.text(fieldName);
		}
		handleError(new IllegalArgumentException("Invalid index field."));
		return null;
	}
	@Override
	public String toString() {
		return "PartialIndex [index=" + index + ", options=" + options + ", rawIndexes=" + rawIndexes + "]";
	}

	private static void handleError(Throwable cause) {
		throw new SmofException(cause);
	}
}
