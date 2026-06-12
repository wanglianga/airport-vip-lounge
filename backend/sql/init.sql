-- Airport VIP Lounge Management System Database Initialization

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS vip_lounge DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE vip_lounge;

-- Passenger table
CREATE TABLE IF NOT EXISTS passenger (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '旅客姓名',
    id_card VARCHAR(30) COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    tier VARCHAR(20) COMMENT '等级: GOLD/PLATINUM/DIAMOND',
    airline VARCHAR(100) COMMENT '航司',
    flight_no VARCHAR(20) COMMENT '航班号',
    seat_no VARCHAR(20) COMMENT '座位号',
    companions INT DEFAULT 0 COMMENT '同行人数',
    has_children BOOLEAN DEFAULT FALSE COMMENT '是否有儿童',
    is_foreigner BOOLEAN DEFAULT FALSE COMMENT '是否外宾',
    dietary_restrictions VARCHAR(500) COMMENT '餐食禁忌',
    need_far_stand BOOLEAN DEFAULT FALSE COMMENT '远机位需求',
    status VARCHAR(20) DEFAULT 'ARRIVED' COMMENT '状态: ARRIVED/IN_LOUNGE/SERVING/BOARDING/LEFT',
    check_in_time DATETIME COMMENT '入住时间',
    boarding_time DATETIME COMMENT '登机时间',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旅客表';

-- Flight table
CREATE TABLE IF NOT EXISTS flight (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    flight_no VARCHAR(20) NOT NULL COMMENT '航班号',
    airline VARCHAR(100) COMMENT '航空公司',
    destination VARCHAR(100) COMMENT '目的地',
    scheduled_departure DATETIME COMMENT '计划起飞时间',
    actual_departure DATETIME COMMENT '实际起飞时间',
    gate VARCHAR(20) COMMENT '登机口',
    is_far_stand BOOLEAN DEFAULT FALSE COMMENT '是否远机位',
    status VARCHAR(20) DEFAULT 'ON_TIME' COMMENT '状态: ON_TIME/DELAYED/CANCELLED/BOARDING/DEPARTED',
    passenger_count INT DEFAULT 0 COMMENT '旅客数量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='航班表';

-- Seat table
CREATE TABLE IF NOT EXISTS seat (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    seat_no VARCHAR(20) NOT NULL COMMENT '座位号',
    area VARCHAR(10) COMMENT '区域: A/B/C/D',
    type VARCHAR(20) COMMENT '类型: SOFA/SEAT/PRIVATE',
    status VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '状态: AVAILABLE/OCCUPIED/MAINTENANCE',
    passenger_id BIGINT COMMENT '旅客ID',
    check_in_time DATETIME COMMENT '入住时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位表';

-- Meal table
CREATE TABLE IF NOT EXISTS meal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    passenger_id BIGINT COMMENT '旅客ID',
    flight_no VARCHAR(20) COMMENT '航班号',
    meal_type VARCHAR(20) COMMENT '类型: HOT_MEAL/DRINK/SNACK/LUNCH_BOX',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING/PREPARING/READY/DELIVERED',
    dietary_requirements VARCHAR(500) COMMENT '饮食要求',
    quantity INT DEFAULT 1 COMMENT '数量',
    prepare_time DATETIME COMMENT '开始准备时间',
    ready_time DATETIME COMMENT '备好时间',
    deliver_time DATETIME COMMENT '送达时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='餐食表';

-- Shower Room table
CREATE TABLE IF NOT EXISTS shower_room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_no VARCHAR(20) NOT NULL COMMENT '房间号',
    status VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '状态: AVAILABLE/OCCUPIED/CLEANING/MAINTENANCE',
    passenger_id BIGINT COMMENT '旅客ID',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='淋浴间表';

-- Meeting Room table
CREATE TABLE IF NOT EXISTS meeting_room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_name VARCHAR(100) NOT NULL COMMENT '会议室名称',
    capacity INT COMMENT '容纳人数',
    status VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '状态: AVAILABLE/OCCUPIED/MAINTENANCE',
    passenger_id BIGINT COMMENT '旅客ID',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    purpose VARCHAR(500) COMMENT '用途',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议室表';

-- Vehicle table
CREATE TABLE IF NOT EXISTS vehicle (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plate_no VARCHAR(20) NOT NULL COMMENT '车牌号',
    vehicle_type VARCHAR(20) COMMENT '类型: SEDAN/VAN/BUS',
    driver_name VARCHAR(50) COMMENT '司机姓名',
    driver_phone VARCHAR(20) COMMENT '司机电话',
    status VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '状态: AVAILABLE/ASSIGNED/ON_ROUTE/RETURNING/MAINTENANCE',
    current_task_id BIGINT COMMENT '当前任务ID',
    current_passenger_id BIGINT COMMENT '当前旅客ID',
    location VARCHAR(200) COMMENT '当前位置',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车辆表';

-- Service Record table
CREATE TABLE IF NOT EXISTS service_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    passenger_id BIGINT COMMENT '旅客ID',
    service_type VARCHAR(20) COMMENT '服务类型: SEAT/SHOWER/MEETING/MEAL/VEHICLE/LUGGAGE/CHILD',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING/IN_PROGRESS/COMPLETED/CANCELLED',
    staff_id BIGINT COMMENT '服务人员ID',
    staff_name VARCHAR(50) COMMENT '服务人员姓名',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务记录表';

-- Staff table
CREATE TABLE IF NOT EXISTS staff (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    role VARCHAR(20) COMMENT '角色: RECEPTION/WAITER/CHEF/DRIVER',
    status VARCHAR(20) DEFAULT 'ON_DUTY' COMMENT '状态: ON_DUTY/OFF_DUTY/BUSY',
    phone VARCHAR(20) COMMENT '电话',
    current_task_count INT DEFAULT 0 COMMENT '当前任务数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务人员表';

-- Insert sample data

-- Sample passengers
INSERT INTO passenger (name, id_card, phone, tier, airline, flight_no, seat_no, companions, has_children, is_foreigner, dietary_restrictions, need_far_stand, status, check_in_time, remark) VALUES
('张三', '110101199001011234', '13800138001', 'GOLD', '中国国航', 'CA1234', '12A', 0, FALSE, FALSE, '无', FALSE, 'IN_LOUNGE', '2024-01-15 08:30:00', 'VIP旅客'),
('李四', '110101199102022345', '13800138002', 'PLATINUM', '东方航空', 'MU5678', '8C', 1, TRUE, FALSE, '素食', TRUE, 'SERVING', '2024-01-15 09:00:00', '需要儿童餐'),
('王五', '110101199203033456', '13800138003', 'DIAMOND', '南方航空', 'CZ9012', '1A', 2, FALSE, TRUE, '清真', FALSE, 'IN_LOUNGE', '2024-01-15 09:30:00', '外宾，英语服务'),
('赵六', '110101199304044567', '13800138004', 'GOLD', '海南航空', 'HU3456', '15B', 0, FALSE, FALSE, '海鲜过敏', FALSE, 'ARRIVED', NULL, '刚到'),
('钱七', '110101199405055678', '13800138005', 'PLATINUM', '中国国航', 'CA7890', '5D', 0, FALSE, FALSE, '无', TRUE, 'BOARDING', '2024-01-15 07:00:00', '准备登机');

-- Sample flights
INSERT INTO flight (flight_no, airline, destination, scheduled_departure, gate, is_far_stand, status, passenger_count) VALUES
('CA1234', '中国国航', '北京', '2024-01-15 11:00:00', 'T2-12', FALSE, 'ON_TIME', 150),
('MU5678', '东方航空', '上海', '2024-01-15 12:30:00', 'T2-15', TRUE, 'ON_TIME', 180),
('CZ9012', '南方航空', '广州', '2024-01-15 14:00:00', 'T1-08', FALSE, 'DELAYED', 200),
('HU3456', '海南航空', '深圳', '2024-01-15 15:30:00', 'T2-20', FALSE, 'ON_TIME', 160),
('CA7890', '中国国航', '成都', '2024-01-15 10:00:00', 'T1-05', TRUE, 'BOARDING', 140);

-- Sample seats
INSERT INTO seat (seat_no, area, type, status, passenger_id) VALUES
('A01', 'A', 'SOFA', 'OCCUPIED', 1),
('A02', 'A', 'SOFA', 'AVAILABLE', NULL),
('A03', 'A', 'SEAT', 'OCCUPIED', 2),
('B01', 'B', 'SEAT', 'AVAILABLE', NULL),
('B02', 'B', 'SEAT', 'AVAILABLE', NULL),
('B03', 'B', 'PRIVATE', 'OCCUPIED', 3),
('C01', 'C', 'SOFA', 'AVAILABLE', NULL),
('C02', 'C', 'SEAT', 'MAINTENANCE', NULL),
('D01', 'D', 'PRIVATE', 'AVAILABLE', NULL),
('D02', 'D', 'PRIVATE', 'AVAILABLE', NULL);

-- Sample meals
INSERT INTO meal (passenger_id, flight_no, meal_type, status, dietary_requirements, quantity) VALUES
(1, 'CA1234', 'HOT_MEAL', 'PENDING', '正常餐', 1),
(2, 'MU5678', 'HOT_MEAL', 'PREPARING', '素食，儿童餐', 2),
(3, 'CZ9012', 'LUNCH_BOX', 'READY', '清真', 3),
(1, 'CA1234', 'DRINK', 'DELIVERED', '咖啡', 1),
(5, 'CA7890', 'SNACK', 'DELIVERED', '坚果', 1);

-- Sample shower rooms
INSERT INTO shower_room (room_no, status, passenger_id) VALUES
('S01', 'AVAILABLE', NULL),
('S02', 'OCCUPIED', 1),
('S03', 'AVAILABLE', NULL),
('S04', 'CLEANING', NULL),
('S05', 'AVAILABLE', NULL);

-- Sample meeting rooms
INSERT INTO meeting_room (room_name, capacity, status, passenger_id, purpose) VALUES
('商务会议室A', 10, 'AVAILABLE', NULL, NULL),
('商务会议室B', 6, 'OCCUPIED', 3, '商务洽谈'),
('小型会议室', 4, 'AVAILABLE', NULL, NULL),
('贵宾厅', 20, 'MAINTENANCE', NULL, '设备维护');

-- Sample vehicles
INSERT INTO vehicle (plate_no, vehicle_type, driver_name, driver_phone, status, current_passenger_id, location) VALUES
('京A12345', 'SEDAN', '王师傅', '13900139001', 'AVAILABLE', NULL, '贵宾楼停车场'),
('京B23456', 'VAN', '李师傅', '13900139002', 'ON_ROUTE', 5, '前往T1航站楼'),
('京C34567', 'BUS', '张师傅', '13900139003', 'AVAILABLE', NULL, '贵宾楼停车场'),
('京D45678', 'SEDAN', '赵师傅', '13900139004', 'RETURNING', NULL, '返回途中'),
('京E56789', 'VAN', '刘师傅', '13900139005', 'MAINTENANCE', NULL, '维修厂');

-- Sample service records
INSERT INTO service_record (passenger_id, service_type, status, staff_id, staff_name, start_time, remark) VALUES
(1, 'SEAT', 'COMPLETED', 1, '接待员小王', '2024-01-15 08:30:00', '安排A区沙发座位'),
(1, 'SHOWER', 'IN_PROGRESS', 2, '服务员小李', '2024-01-15 09:00:00', '使用S02淋浴间'),
(2, 'MEAL', 'PENDING', 3, '厨师长张', NULL, '准备素食和儿童餐'),
(3, 'MEETING', 'COMPLETED', 1, '接待员小王', '2024-01-15 09:30:00', '预订商务会议室B'),
(5, 'VEHICLE', 'IN_PROGRESS', 4, '司机李师傅', '2024-01-15 09:45:00', '送往T1登机口'),
(2, 'CHILD', 'COMPLETED', 2, '服务员小李', '2024-01-15 09:00:00', '提供儿童看护服务');

-- Sample staff
INSERT INTO staff (name, role, status, phone, current_task_count) VALUES
('小王', 'RECEPTION', 'ON_DUTY', '13700137001', 0),
('小李', 'WAITER', 'BUSY', '13700137002', 2),
('张厨师', 'CHEF', 'ON_DUTY', '13700137003', 1),
('李师傅', 'DRIVER', 'BUSY', '13700137004', 1),
('小陈', 'RECEPTION', 'OFF_DUTY', '13700137005', 0);
