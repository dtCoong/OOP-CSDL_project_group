package electronic_health_record.Hospital;

import java.util.List;
import java.util.Optional;

public interface HospitalDao {

    /**
     * Thêm một bệnh viện mới vào CSDL.
     * @param hospital Đối tượng Hospital cần thêm.
     */
    void addHospital(Hospital hospital);

    /**
     * Lấy thông tin bệnh viện bằng ID.
     * @param hospitalId ID của bệnh viện.
     * @return Optional chứa Hospital nếu tìm thấy.
     */
    Optional<Hospital> getHospitalById(int hospitalId);

    /**
     * Lấy danh sách tất cả bệnh viện.
     * @return List các đối tượng Hospital.
     */
    List<Hospital> getAllHospitals();

    /**
     * Cập nhật thông tin của một bệnh viện.
     * @param hospital Đối tượng Hospital với thông tin đã cập nhật.
     */
    void updateHospital(Hospital hospital);

    /**
     * Xóa một bệnh viện khỏi CSDL.
     * @param hospitalId ID của bệnh viện cần xóa.
     */
    void deleteHospital(int hospitalId);
}