
//用户资料响应类型 (已改造为ResponseDTO格式)
export type ResponseType = {
    code: number;
    message: string;
    data: {
        id: string;
        nickname: string;
        avatar: string;
        vipLevel: string;
        coupons: number;
        rewardPoints: number;
        phoneNumber: string;
        email: string;
    };
}