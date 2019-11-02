//
//  GifTableViewCell.swift
//  ios-app
//
//  Created by Руслан Алиев on 02/11/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import MultiPlatformLibraryUnits
import SwiftyGif

class GifTableViewCell: UITableViewCell, Fillable {
    typealias DataType = CellModel
    
    struct CellModel {
        let id: Int64
        let gifUrl: String
    }
    
    @IBOutlet private var gifImageView: UIImageView!
    
    private var gifDownloadTask: URLSessionDataTask?
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        gifDownloadTask?.cancel()
        gifImageView.clear()
    }
    
    func fill(_ data: GifTableViewCell.CellModel) {
        gifDownloadTask = gifImageView.setGifFromURL(URL(string: data.gifUrl)!)
    }
    
    func update(_ data: GifTableViewCell.CellModel) {
        
    }
}

extension GifTableViewCell: Reusable {
    static func reusableIdentifier() -> String {
        return "GifTableViewCell"
    }
    
    static func xibName() -> String {
        return "GifTableViewCell"
    }
    
    static func bundle() -> Bundle {
        return Bundle.main
    }
}
