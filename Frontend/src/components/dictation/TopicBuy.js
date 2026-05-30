import { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { http } from "../../api/Http";

const TopicBuy = () => {
    const [topics, setTopics] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [hasOrder, setHasOrder] = useState(null); // Để theo dõi trạng thái đơn hàng
    const navigate = useNavigate();

    // Lấy userId từ localStorage
    const getUserId = () => {
        const userId = localStorage.getItem("userId"); // Lấy userID từ localStorage
        if (!userId) {
            navigate("/login"); // Nếu không có userId, chuyển hướng đến trang đăng nhập
            return null;
        }
        return userId;
    };

    // Dữ liệu giả
    const fakeTopics = [
        {
            id: 6,
            type: "IELTS Listening ++",
            level: "A1-A2",
            img: "https://res.cloudinary.com/dc3o5kumf/image/upload/v1748135121/rtarcxisycl9fagokv3o.jpg",
            countTopic: 5
        },
        {
            id: 7,
            type: "TOEIC Listening ++",
            level: "A1",
            img: "https://res.cloudinary.com/dc3o5kumf/image/upload/v1748134944/sekap1jcnvw6nib2ezrm.jpg",
            countTopic: 3
        },
        {
            id: 8,
            type: "Short Story ++",
            level: "B1-B2",
            img: "https://res.cloudinary.com/dc3o5kumf/image/upload/v1748135077/z5jijllzjw9vwymrdfeu.jpg",
            countTopic: 2
        },
    ];

    const fetchTopics = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);

            // Gọi API
            const response = await http("/api/show-all-topic");

            // Log lại toàn bộ dữ liệu để kiểm tra
            console.log("API Response:", response);

            // Kiểm tra xem dữ liệu có đúng không
            if (!response || !response.data || !response.data.result) {
                throw new Error("Dữ liệu không có kết quả");
            }

            // Kiểm tra nếu result là mảng và có ít nhất 1 phần tử
            if (!Array.isArray(response.data.result) || response.data.result.length === 0) {
                throw new Error("Dữ liệu không có kết quả");
            }

            // Cập nhật danh sách chủ đề với dữ liệu từ API
            let fetchedTopics = response.data.result;

            // Nếu số lượng chủ đề ít hơn 8, bổ sung dữ liệu giả
            if (fetchedTopics.length < 8) {
                const remainingTopics = fakeTopics.slice(0, 8 - fetchedTopics.length);
                fetchedTopics = [...fetchedTopics, ...remainingTopics];
            }

            setTopics(fetchedTopics);
        } catch (err) {
            console.error("Error fetching topics:", err);
            setError(err.message); // Lưu thông báo lỗi
            // Nếu có lỗi, sử dụng dữ liệu giả
            let fetchedTopics = fakeTopics;
            if (fetchedTopics.length < 8) {
                const remainingTopics = fakeTopics.slice(0, 8 - fetchedTopics.length);
                fetchedTopics = [...fetchedTopics, ...remainingTopics];
            }
            setTopics(fetchedTopics);
        } finally {
            setLoading(false); // Dừng loading
        }
    }, []);

    // Kiểm tra đơn hàng của người dùng
    const checkOrder = useCallback(async () => {
        const userId = getUserId();
        if (!userId) return;

        try {
            const response = await http(`/api/check-order?userId=${userId}`);
            if (response.data && response.data.code === 200 && response.data.result === true) {
                setHasOrder(true);
            } else {
                setHasOrder(false);
            }
        } catch (err) {
            console.error("Error checking order:", err);
            setHasOrder(false);
        }
    }, [navigate]);

    useEffect(() => {
        checkOrder();
    }, [checkOrder]);

    useEffect(() => {
        if (hasOrder === null) {
            return;
        }

        if (hasOrder) {
            fetchTopics();
        } else {
            navigate("/payment");
        }
    }, [hasOrder, fetchTopics, navigate]);

    const LoadingComponent = () => (
        <div className="min-h-screen bg-gradient-to-br from-teal-100 to-teal-200 flex justify-center items-center">
            <div className="text-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-4 border-teal-600 mx-auto mb-4"></div>
                <p className="text-teal-600 text-xl font-medium">Đang tải dữ liệu...</p>
                <p className="text-teal-500 text-sm mt-2">Vui lòng chờ trong giây lát</p>
            </div>
        </div>
    );

    const ErrorComponent = () => (
        <div className="min-h-screen bg-gradient-to-br from-red-100 to-red-200 flex justify-center items-center">
            <div className="text-center max-w-md mx-auto p-6">
                <div className="bg-red-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                    <span className="text-red-600 text-2xl">⚠️</span>
                </div>
                <h2 className="text-xl font-semibold text-red-600 mb-2">Đã xảy ra lỗi</h2>
                <p className="text-red-500 mb-4">{error}</p>
                <button onClick={fetchTopics} className="bg-red-600 hover:bg-red-700 text-white px-6 py-2 rounded-lg transition-colors duration-200">
                    Thử lại
                </button>
            </div>
        </div>
    );

    const TopicCard = ({ topic }) => {
        const [imageError, setImageError] = useState(false);

        const handleImageError = () => {
            setImageError(true);
        };

        return (
            <div className="group bg-white shadow-lg hover:shadow-2xl p-6 rounded-3xl flex flex-col items-center transition-all duration-300 transform hover:-translate-y-1 border border-gray-100 hover:shadow-lg">
                <div className="flex-shrink-0 mb-4">
                    {!imageError ? (
                        <img
                            src={topic.img || "/placeholder.svg"}
                            alt={topic.type}
                            className="w-32 h-32 rounded-xl object-cover group-hover:scale-105 transition-transform duration-300"
                            onError={handleImageError}
                        />
                    ) : (
                        <div className="w-32 h-32 rounded-xl bg-gradient-to-br from-teal-100 to-teal-200 flex items-center justify-center">
                            <span className="text-2xl">🎧</span>
                        </div>
                    )}
                </div>

                <h3 className="text-xl font-bold text-teal-700 group-hover:text-teal-800 transition-colors duration-200 mb-2">{topic.type}</h3>
                <div className="mt-2 mb-3">
                    <span className="inline-block bg-teal-100 text-teal-800 text-xs font-medium px-3 py-1 rounded-full">Cấp độ: {topic.level}</span>
                </div>
                <div className="text-sm text-gray-600 flex items-center space-x-2">
                    <span className="text-lg">🎧</span>
                    <span className="font-medium">{topic.countTopic} bài học</span>
                </div>
                <div className="mt-4 w-full text-center">
                    <button
                        className="bg-teal-600 hover:bg-teal-700 text-white px-6 py-2 rounded-lg transition-colors duration-200 w-full"
                        onClick={() => alert(`Chọn chủ đề: ${topic.type}`)} // Hành động khi chọn chủ đề
                    >
                        Xem chi tiết
                    </button>
                </div>
            </div>
        );
    };

    if (loading) {
        return <LoadingComponent />;
    }

    if (error) {
        return <ErrorComponent />;
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-teal-50 to-teal-100 py-12">
            <div className="max-w-7xl mx-auto px-6">
                <div className="text-center mb-12">
                    <h1 className="text-5xl font-bold mb-4 bg-gradient-to-r from-teal-600 to-teal-800 bg-clip-text text-transparent">
                        Tất cả chủ đề
                    </h1>
                    <p className="text-teal-600 text-lg max-w-2xl mx-auto">
                        Khám phá các chủ đề học tập đa dạng và phong phú để nâng cao kiến thức của bạn
                    </p>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 mt-12">
                    {topics.map((topic, index) => (
                        <TopicCard key={index} topic={topic} />
                    ))}
                </div>
            </div>
        </div>
    );
};

export default TopicBuy;
