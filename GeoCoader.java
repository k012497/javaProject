package APItest;

public class GeoCoader {
	@RequestMapping(value="/sample/getAddrApi.do") public void getAddrApi(HttpServletRequest req, 
			HttpServletResponse response){ 
			// 요청변수 설정 String admCd = req.getParameter("admCd"); 
			String rnMgtSn = req.getParameter("rnMgtSn"); String udrtYn = req.getParameter("udrtYn"); String buldMnnm = req.getParameter("buldMnnm"); String buldSlno = req.getParameter("buldSlno"); String confmKey = req.getParameter("confmKey"); 
			// API 호출 URL 정보 설정 String apiUrl = "http://www.juso.go.kr/addrlink/addrCoordApi.do? 
			admCd ="+admCd+ "&rnMgtSn="+rnMgtSn+"&udrtYn="+udrtYn+ "&buldMnnm="+buldMnnm+"&buldSlno="+buldSlno+ "&confmKey="+confmKey; 
			URL url = new URL(apiUrl); BufferedReader br = new BufferedReader( 
			                      new InputStreamReader(
			url.openStream(),"UTF-8")); StringBuffer sb = new StringBuffer(); 
			String tempStr = null; while(true){ 
			tempStr = br.readLine(); if(tempStr == null) break; sb.append(tempStr); // 응답결과 XML 저장 
			} br.close(); response.setCharacterEncoding("UTF-8"); response.setContentType("text/xml"); response.getWriter().write(sb.toString()); // 응답결과 반환 
			} 

}
