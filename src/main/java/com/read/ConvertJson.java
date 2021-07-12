package com.read;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ConvertJson {

    public static void main(String[] args) throws IOException {

        List<JsonDTO> list = readFromExcel();
        Map<String, List<JsonDTO>> containerGroup = list.stream().collect(Collectors.groupingBy(JsonDTO::getContainer));
        createJsonFile(containerGroup);
    }

    private static void createJsonFile(Map<String, List<JsonDTO>> containerGroup) throws JsonProcessingException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("applicationName", "Crunchify.com");
        map.put("collectionName", "App Shah");
        JSONArray containers = new JSONArray();
        int id = 0;
        for(Map.Entry entry : containerGroup.entrySet()){
           // System.out.println(entry.getKey());
            //JSONObject containerobj = new JSONObject();
            LinkedHashMap<String, Object> containerobj = new LinkedHashMap<String, Object>();
            containerobj.put("id",id++);
            containerobj.put("name",entry.getKey());
            containerobj.put("statement","");
            List<JsonDTO> dtos = (List<JsonDTO>) entry.getValue();
            JSONArray sourcetables = new JSONArray();
            List<String> tables = dtos.stream().distinct().map(JsonDTO::getSourcetable).collect(Collectors.toList());
            tables.forEach(table -> sourcetables.add(table));
            containerobj.put("sourceTable",sourcetables);
            JSONArray mappings = new JSONArray();

            for (JsonDTO dto : dtos) {
                //JSONObject mapping = new JSONObject();
                //JSONObject source = new JSONObject();
                LinkedHashMap<String, Object> mapping = new LinkedHashMap<String, Object>();
                LinkedHashMap<String, Object> source = new LinkedHashMap<String, Object>();
                source.put("column",dto.getColumn());
                source.put("table",dto.getSourcetable());
                mapping.put("source",source);

                LinkedHashMap<String, Object> target = new LinkedHashMap<String, Object>();
                //JSONObject target = new JSONObject();
                target.put("jpath",dto.getTargetPath());
                target.put("dataType",dto.getDataType());
                mapping.put("target",target);
                mappings.add(mapping);
                //System.out.println(mapping.toJSONString());

            }
            containerobj.put("mappings",mappings);
            containers.add(containerobj);
           // System.out.println(containerobj.toJSONString());
        }
        map.put("container",containers);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        System.out.println(json);
       // System.out.println(obj.toJSONString());
    }

    private static List<JsonDTO> readFromExcel() throws IOException {

        FileInputStream file = new FileInputStream(new File("C:\\Kiran\\test_read.xlsx"));

        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        //Iterate through each rows one by one
        Iterator<Row> iterator = sheet.iterator();

        List<JsonDTO> jsonDTOS = new ArrayList<JsonDTO>();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            if(nextRow.getRowNum() == 0){
                continue;
            }
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            JsonDTO aBook = new JsonDTO();

            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();
                int columnIndex = nextCell.getColumnIndex();

                switch (columnIndex) {
                    case 0:
                        aBook.setSourcetable((String) getCellValue(nextCell));
                        break;
                    case 1:
                        aBook.setColumn((String) getCellValue(nextCell));
                        break;
                    case 2:
                        aBook.setTargetPath((String) getCellValue(nextCell));
                        break;
                    case 3:
                        aBook.setDataType((String) getCellValue(nextCell));
                        break;
                    case 4:
                        aBook.setContainer((String) getCellValue(nextCell));
                        break;
                }


            }
            jsonDTOS.add(aBook);
        }
        return jsonDTOS;
    }

    private static Object getCellValue(Cell cell) {

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();

            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();

            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
        }
        return null;
    }
}
