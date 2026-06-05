"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "../ui/card"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../ui/select"

export default function TranslationBox({ translation = { en: "No translation available", vi: "Chưa có bản dịch" } }) {
    const [selectedLang, setSelectedLang] = useState("en")

    const languageLabels = {
        en: "English",
        vi: "Vietnamese",
    }

    const uiText = {
        title: {
            en: "🌐 Bản dịch",
        },
        empty: {
            en: "There are no translations available.",
        },
    }

    useEffect(() => {
        console.log("New translation received: ", translation)
    }, [translation]) // Khi translation thay đổi, nó sẽ re-render và log dữ liệu mới.

    return (
        <Card className="p-4 border rounded-lg bg-white shadow-sm">
            <CardHeader className="p-0 pb-2">
                <CardTitle className="text-lg font-semibold text-gray-800">{uiText.title["en"]}</CardTitle>
            </CardHeader>
            <CardContent className="p-0">
                <div className="mb-4">
                    <Select value={selectedLang} onValueChange={setSelectedLang}>
                        <SelectTrigger className="w-full">
                            <SelectValue placeholder="Chọn ngôn ngữ" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="en">{languageLabels["en"]}</SelectItem>
                            <SelectItem value="vi">{languageLabels["vi"]}</SelectItem>
                        </SelectContent>
                    </Select>
                </div>

                <div className="space-y-4">
                    <div className="text-gray-800">
                        <p className="font-medium text-lg">{translation[selectedLang]}</p>
                    </div>
                </div>
            </CardContent>
        </Card>
    )
}
