package com.comcast.ebi.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Parser {
	
	public static StringBuffer sb = new StringBuffer();
	

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("in main method");

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		map = mapper.readValue(new File("sample_json.txt"), new TypeReference<Map<String,Object>>() {});
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext())
		{
			String key = it.next();
			Object value = map.get(key);
			if(value instanceof LinkedHashMap<?, ?>){

				gotolinkedhashmap((LinkedHashMap<?,?>)value);			
			}else if ( value instanceof ArrayList<?>)
			{
				gotoarraylist((ArrayList<?>)value);
			}else if(value instanceof String)
			{
				//System.out.println(key + ":  "  + value.toString());
				if(sb.length()==0)
				{
					sb.append(value);
				}else
				{
					sb.append(","+value);
				}
			}
		}
		printascsv(sb);
		

	}

	private static void printascsv(StringBuffer sb2) {
		
	
		System.out.print(sb2.toString());;
	}
		


	private static void gotolinkedhashmap(LinkedHashMap<?,?> value) {

		Iterator<?> it1 =  value.keySet().iterator();
		while(it1.hasNext())
		{
			String k = (String) it1.next();
			if(value.get(k) instanceof String || value.get(k) == null)
			{
				if(sb.length()==0)
				{
					sb.append(value.get(k));
				}else
				{
					sb.append(","+value.get(k));
				}
				//System.out.println(k + ":" + value.get(k));
			}else if(value.get(k) instanceof ArrayList<?>){
				gotoarraylist((ArrayList<?>)value.get(k));
				
			} else if(value.get(k) instanceof LinkedHashMap<?, ?>)
			{
				gotolinkedhashmap((LinkedHashMap<?, ?>)value.get(k));
			}
		}



	}

	private static void gotoarraylist(ArrayList<?> value) {
		if(!(value.size()==0))
		{
			for(int i=0;i<=value.size()-1;i++){
				if(value.get(i) instanceof ArrayList<?>)
				{
					gotoarraylist( (ArrayList<?>)value.get(i));
				}else if(value.get(i) instanceof LinkedHashMap<?, ?>)
				{
					gotolinkedhashmap( (LinkedHashMap<?, ?>)value.get(i));
				}else if(value.get(i) instanceof String  || value.get(i) == null)
				{
					if(sb.length()==0)
					{
						sb.append(value.get(i));
					}else
					{
						sb.append(","+value.get(i));
					}
				}
			}
		}else{
			System.out.println("Array is empty");
		}
	}
}


