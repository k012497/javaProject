package controller;

import java.text.DecimalFormat;
import java.text.ParsePosition;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class SharedMethod {

	public static void alertDisplay(int type, String title, String headerText, String contentText) {
		Alert alert = null;
		switch (type) {
		case 1:
			alert = new Alert(AlertType.WARNING);
			break;
		case 2:
			alert = new Alert(AlertType.CONFIRMATION);
			break;
		case 3:
			alert = new Alert(AlertType.ERROR);
			break;
		case 4:
			alert = new Alert(AlertType.NONE);
			break;
		case 5:
			alert = new Alert(AlertType.INFORMATION);
			break;
		}
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(headerText + "\n" + contentText);
		alert.setResizable(false);
		alert.showAndWait();
	}// end of alertDisplay

	// 텍스트필드 입력값 포맷설정기능(2자리 숫자만 받음)
	public static void inputDecimalFormatTwoDigit(TextField textField) {
		// 숫자만 입력(정수만 입력받음), 실수입력받고싶으면new DecimalFormat("###.#");
		DecimalFormat format = new DecimalFormat("##");
		// 점수 입력시 길이 제한 이벤트 처리
		textField.setTextFormatter(new TextFormatter<>(event -> {
			// 입력받은 내용이 없으면 이벤트를 리턴함.
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			// 구문을 분석할 시작 위치를 지정함. 세자리까지 계속해서 점검하겠다.
			ParsePosition parsePosition = new ParsePosition(0);
			// 입력받은 내용과 분석위치를 지정한지점부터 format 내용과 일치한지 분석함.
			Object object = format.parse(event.getControlNewText(), parsePosition);
			// 리턴값이 null 이거나,입력한길이와 구문분석위치값이 적어버리면(다 분석하지못했음을 뜻함)거나,입력한길이가 4이면(3자리를 넘었음을
			// 뜻함.) 이면 null 리턴함.
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 3) {
				return null;
			} else {
				return event; // 값을 돌려주겠다.
			}
		}));
	}

	// 텍스트필드 입력값 포맷설정기능(4자리 숫자만 받음)
	public static void inputDecimalFormatFourDigit(TextField textField) {
		// 숫자만 입력(정수만 입력받음), 실수입력받고싶으면new DecimalFormat("###.#");
		DecimalFormat format = new DecimalFormat("####");
		// 점수 입력시 길이 제한 이벤트 처리
		textField.setTextFormatter(new TextFormatter<>(event -> {
			// 입력받은 내용이 없으면 이벤트를 리턴함.
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			// 구문을 분석할 시작 위치를 지정함. 세자리까지 계속해서 점검하겠다.
			ParsePosition parsePosition = new ParsePosition(0);
			// 입력받은 내용과 분석위치를 지정한지점부터 format 내용과 일치한지 분석함.
			Object object = format.parse(event.getControlNewText(), parsePosition);
			// 리턴값이 null 이거나,입력한길이와 구문분석위치값이 적어버리면(다 분석하지못했음을 뜻함)거나,입력한길이가 4이면(3자리를 넘었음을
			// 뜻함.) 이면 null 리턴함.
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 5) {
				return null;
			} else {
				return event; // 값을 돌려주겠다.
			}
		}));
	}

	// 텍스트필드 입력값 포맷설정기능(6자리 숫자만 받음)
	public static void inputDecimalFormatSixDigit(TextField textField) {
		// 숫자만 입력(정수만 입력받음), 실수입력받고싶으면new DecimalFormat("###.#");
		DecimalFormat format = new DecimalFormat("######");
		// 점수 입력시 길이 제한 이벤트 처리
		textField.setTextFormatter(new TextFormatter<>(event -> {
			// 입력받은 내용이 없으면 이벤트를 리턴함.
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			// 구문을 분석할 시작 위치를 지정함. 세자리까지 계속해서 점검하겠다.
			ParsePosition parsePosition = new ParsePosition(0);
			// 입력받은 내용과 분석위치를 지정한지점부터 format 내용과 일치한지 분석함.
			Object object = format.parse(event.getControlNewText(), parsePosition);
			// 리턴값이 null 이거나,입력한길이와 구문분석위치값이 적어버리면(다 분석하지못했음을 뜻함)거나,입력한길이가 4이면(3자리를 넘었음을
			// 뜻함.) 이면 null 리턴함.
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 7) {
				return null;
			} else {
				return event; // 값을 돌려주겠다.
			}
		}));
	}

	// 텍스트필드 입력값 포맷설정기능(13자리 숫자만 받음)
	public static void inputDecimalFormatThirteenDigit(TextField textField) {
		// 숫자만 입력(정수만 입력받음), 실수입력받고싶으면new DecimalFormat("###.#");
		DecimalFormat format = new DecimalFormat("##############");
		// 점수 입력시 길이 제한 이벤트 처리
		textField.setTextFormatter(new TextFormatter<>(event -> {
			// 입력받은 내용이 없으면 이벤트를 리턴함.
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			// 구문을 분석할 시작 위치를 지정함. 세자리까지 계속해서 점검하겠다.
			ParsePosition parsePosition = new ParsePosition(0);
			// 입력받은 내용과 분석위치 를 지정한지점부터 format 내용과 일치한지 분석함.
			Object object = format.parse(event.getControlNewText(), parsePosition);
			// 리턴값이 null 이거나,입력한길이와 구문분석위치값이 적어버리면(다 분석하지못했음을 뜻함)거나,입력한길이가 4이면(3자리를 넘었음을
			// 뜻함.) 이면 null 리턴함.
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 12) {
				return null;
			} else {
				return event; // 값을 돌려주겠다.
			}
		}));
	}

}
