package com.example.dailydictation.controller;

import com.example.dailydictation.config.VNPayConfig;
import com.example.dailydictation.entity.Order;
import com.example.dailydictation.entity.User;
import com.example.dailydictation.enums.EStatusVNPay;
import com.example.dailydictation.mapper.OrderRepository;
import com.example.dailydictation.repository.UserRepository;
import com.example.dailydictation.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private VNPayConfig vnPayConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    // 👉 Tạo link thanh toán
    @GetMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestParam("amount") int amount,
                                           @RequestParam("userId") int userId) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String vnp_TxnRef = VNPayUtils.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = vnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // VNPay yêu cầu nhân 100
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        // 👇 Gắn userId vào OrderInfo
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef + "-" + userId);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", VNPayUtils.getCurrentDate());

        String queryUrl = VNPayUtils.buildQuery(vnp_Params);
        String hash = VNPayUtils.hmacSHA512(vnPayConfig.vnp_HashSecret, VNPayUtils.buildHashData(vnp_Params));
        String paymentUrl = vnPayConfig.vnp_PayUrl + "?" + queryUrl + "&vnp_SecureHash=" + hash;

        return ResponseEntity.ok(paymentUrl);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<String> paymentReturn(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> fields = new TreeMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldName.startsWith("vnp_")) {
                fields.put(fieldName, fieldValue);
            }
        }

        String receivedHash = fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        String hashData = VNPayUtils.buildHashData(fields);
        String calculatedHash = VNPayUtils.hmacSHA512(vnPayConfig.vnp_HashSecret, hashData);

        if (calculatedHash.equalsIgnoreCase(receivedHash)) {
            String responseCode = fields.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                // ✅ Parse userId từ vnp_OrderInfo
                String orderInfo = fields.get("vnp_OrderInfo"); // "Thanh toan don hang: 12345678-7"
                int userId = extractUserId(orderInfo);

                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();

                    Order order = Order.builder()
                            .user(user)
                            .createDate(LocalDate.now())
                            .eStatusVNPay(EStatusVNPay.OPEN) // hoặc SUCCESS tùy logic bạn
                            .build();

                    orderRepository.save(order);

                    // Trả về HTML chứa liên kết redirect về trang chủ sau khi thanh toán thành công
                    String redirectUrl = "http://localhost:3000/homepage"; // Đảm bảo đây là URL trang chủ của bạn

                    String htmlResponse = "<html><body>"
                            + "<h2>Giao dịch thành công!</h2>"
                            + "<p>Thanh toán của bạn đã được thực hiện thành công. Nhấp vào liên kết dưới đây để trở về trang chủ:</p>"
                            + "<a href='" + redirectUrl + "'>Về trang chủ</a>"
                            + "</body></html>";

                    return ResponseEntity.ok(htmlResponse); // Trả về HTML cho người dùng
                } else {
                    return ResponseEntity.status(404).body("❌ Không tìm thấy user với ID: " + userId);
                }
            } else {
                // Trả về HTML chứa liên kết khi giao dịch thất bại
                String redirectUrl = "http://localhost:3000/homepage"; // Đảm bảo đây là URL trang chủ của bạn

                String htmlResponse = "<html><body>"
                        + "<h2>Giao dịch thất bại!</h2>"
                        + "<p>Giao dịch của bạn đã không thành công. Nhấp vào liên kết dưới đây để quay lại trang chủ:</p>"
                        + "<a href='" + redirectUrl + "'>Về trang chủ</a>"
                        + "</body></html>";

                return ResponseEntity.ok(htmlResponse); // Trả về HTML cho người dùng khi giao dịch thất bại
            }
        }

        // Nếu chữ ký sai, trả về lỗi với liên kết
        String redirectUrl = "http://localhost:3000/homepage"; // Đảm bảo đây là URL trang chủ của bạn
        String htmlResponse = "<html><body>"
                + "<h2>Giao dịch không hợp lệ!</h2>"
                + "<p>Giao dịch của bạn không hợp lệ. Nhấp vào liên kết dưới đây để quay lại trang chủ:</p>"
                + "<a href='" + redirectUrl + "'>Về trang chủ</a>"
                + "</body></html>";

        return ResponseEntity.badRequest().body(htmlResponse); // Trả về lỗi và link quay lại trang chủ
    }

    private int extractUserId(String orderInfo) {
        try {
            // "Thanh toan don hang: 12345678-7"
            String[] parts = orderInfo.split("-");
            return Integer.parseInt(parts[1].trim());
        } catch (Exception e) {
            throw new RuntimeException("❌ Không thể lấy userId từ orderInfo: " + orderInfo);
        }
    }
}
