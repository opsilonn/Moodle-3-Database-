package GUI_Components;

import com.github.lgooddatepicker.components.DatePickerSettings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFunctions {

    public static DatePickerSettings customDates(){
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setAllowKeyboardEditing(false);
        dateSettings.setAllowEmptyDates(false);
        dateSettings.setFormatForDatesCommonEra("dd/MM/yyyy");
        dateSettings.setFormatForDatesBeforeCommonEra("dd/MM/uuuu");
        return dateSettings;
    }

    public static LocalDate convertDate(String date2Convert){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(date2Convert, formatter);
        return date;
    }
}
