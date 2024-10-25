package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BookingStatus;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqOrders;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/addOrder")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Buyurtma qo'shish", description = "Foydalanuvchi o'zining buyurtmasini qo'shishi mumkin.")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody ReqOrders reqOrders, @CurrentUser User user) {
        ApiResponse apiResponse = orderService.addOrder(reqOrders, user);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{orderId}/changeStatus")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Buyurtma statusini o'zgartirish", description = "Foydalanuvchi buyurtma statusini yangilashi mumkin.")
    public ResponseEntity<ApiResponse> changeOrderStatus(@PathVariable Long orderId, @RequestParam BookingStatus status) {
        ApiResponse apiResponse = orderService.changeStatus(orderId, status);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Foydalanuvchining buyurtmalarini olish", description = "Foydalanuvchi o'zining barcha buyurtmalarini ko'rishi mumkin.")
    public ResponseEntity<ApiResponse> getUserOrders(@CurrentUser User user) {
        ApiResponse apiResponse = orderService.getAllOrdersByUser(user);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @Operation(summary = "Barcha buyurtmalarni ko'rish", description = "Admin va super admin huquqiga ega foydalanuvchilar barcha buyurtmalarni ko'rishi mumkin.")
    public ResponseEntity<ApiResponse> getAllOrders(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {
        ApiResponse apiResponse = orderService.getAllOrders(size, page);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update/{orderId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Buyurtmani yangilash", description = "Foydalanuvchi buyurtmasini yangilashi mumkin.")
    public ResponseEntity<ApiResponse> updateOrder(@PathVariable Long orderId, @RequestBody ReqOrders reqOrders, @CurrentUser User user) {
        ApiResponse apiResponse = orderService.updateOrder(orderId, reqOrders, user);
        return ResponseEntity.ok(apiResponse);
    }
}
